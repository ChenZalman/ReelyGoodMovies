package com.example.reelygoodmovies.ui.add_edit_movie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class EditViewModel(application: Application) : AndroidViewModel(application) {

    private val _favorite = MutableLiveData<Boolean>()
    val favorite: LiveData<Boolean> get() = _favorite

    private val _selectedYear = MutableLiveData<Int>()
    val selectedYear: LiveData<Int> get() = _selectedYear

    private val _selectedRuntimeHours = MutableLiveData<Int>()
    val selectedRuntimeHours: LiveData<Int> get() = _selectedRuntimeHours

    private val _selectedRuntimeMinutes = MutableLiveData<Int>()
    val selectedRuntimeMinutes: LiveData<Int> get() = _selectedRuntimeMinutes

    private val _selectedImageURI = MutableLiveData<String>()
    val selectedImageURI: LiveData<String> get() = _selectedImageURI

    private val _isEditMode = MutableLiveData(false)
    val isEditMode: LiveData<Boolean> get() = _isEditMode

    fun setEditMode(isEdit: Boolean) {
        _isEditMode.value = isEdit
    }

    fun setSelectedImageURI(uri: String?) {
        _selectedImageURI.value = uri ?: ""
    }

    fun setSelectedRuntimeHours(hours: Int) {
        _selectedRuntimeHours.value = hours
    }

    fun setSelectedRuntimeMinutes(minutes: Int) {
        _selectedRuntimeMinutes.value = minutes
    }

    fun setSelectedYear(year: Int) {
        _selectedYear.value = year
    }

    fun clearAllData() {
        setSelectedYear(0)
        setSelectedRuntimeHours(0)
        setSelectedRuntimeMinutes(0)
        setSelectedImageURI(null)
        setFavorite(false)
    }

    fun setFavorite(b: Boolean) {
        _favorite.value = b
    }

}