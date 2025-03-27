package com.example.reelygoodmovies.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers

fun <T, A> performFetchingAndSaving(
    localDbFetch: () -> LiveData<T>,
    remoteDbFetch: suspend () -> Resource<A>,
    localDbSave: suspend (A) -> Unit
): LiveData<Resource<T>> =

    liveData(Dispatchers.IO) {

        emit(Resource.loading())

        val source = localDbFetch().map { Resource.success(it) }
        emitSource(source)

        val fetchResource = remoteDbFetch()

        when (fetchResource.status) {
            is Success -> {
                localDbSave(fetchResource.status.data!!)
            }

            is Error -> {
                emit(Resource.error(fetchResource.status.message))
                emitSource(source)
            }

            is Loading -> {
                emit(Resource.loading())
            }
        }
    }