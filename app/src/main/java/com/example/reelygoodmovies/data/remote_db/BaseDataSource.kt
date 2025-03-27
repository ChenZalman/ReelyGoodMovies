package com.example.reelygoodmovies.data.remote_db

import com.example.reelygoodmovies.utils.ErrorType
import com.example.reelygoodmovies.utils.Resource
import retrofit2.Response

abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val result = call()
            if (result.isSuccessful) {
                val body = result.body()
                if (body != null) return Resource.success(body)
            }

            return Resource.error(
                when (result.code()) {
                    404 -> ErrorType.NOT_FOUND_ERROR
                    400 -> ErrorType.BAD_REQUEST_ERROR
                    500 -> ErrorType.INTERNAL_SERVER_ERROR
                    else -> ErrorType.UNKNOWN_ERROR
                }
            )
        } catch (e: Exception) {
            return Resource.error(
                ErrorType.NETWORK_ERROR
            )
        }
    }
}
