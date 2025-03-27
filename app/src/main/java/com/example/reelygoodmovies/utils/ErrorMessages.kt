package com.example.reelygoodmovies.utils

import android.content.Context
import com.example.reelygoodmovies.R

object ErrorMessages {
    fun getErrorMessage(context: Context, errorType: ErrorType): String {
        return when (errorType) {
            ErrorType.NETWORK_ERROR -> context.getString(R.string.network_error)
            ErrorType.NOT_FOUND_ERROR -> context.getString(R.string.not_found_error)
            ErrorType.BAD_REQUEST_ERROR -> context.getString(R.string.bad_request_error)
            ErrorType.INTERNAL_SERVER_ERROR -> context.getString(R.string.internal_server_error)
            ErrorType.UNKNOWN_ERROR -> context.getString(R.string.unknown_error)
        }
    }
}
