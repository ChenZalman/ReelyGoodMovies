package com.example.reelygoodmovies.utils

class Resource <out T> private constructor(val status: Status<T>) {
    companion object{
        fun <T> success(data :T) = Resource(Success(data))
        fun <T> error(errorType: ErrorType , data :T? = null) = Resource(Error(errorType,data))
        fun <T> loading(data : T? = null) = Resource(Loading(data))
    }
}

sealed class Status <out T>(val data : T? = null)

class Success<T>(data : T) : Status<T>(data)
class Error<T>(val message : ErrorType, data: T? = null) : Status<T>(data)
class Loading<T>(data : T? = null) : Status <T>(data)