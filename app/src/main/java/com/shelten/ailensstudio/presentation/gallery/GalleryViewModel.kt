package com.shelten.ailensstudio.presentation.gallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shelten.ailensstudio.data.model.GalleryImage
import com.shelten.ailensstudio.data.repository.GalleryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GalleryViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        GalleryRepository(
            application.contentResolver
        )

    private val _images =
        MutableStateFlow<List<GalleryImage>>(emptyList())

    val images: StateFlow<List<GalleryImage>> =
        _images.asStateFlow()

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error.asStateFlow()

    fun loadImages() {

        viewModelScope.launch(Dispatchers.IO) {

            _isLoading.value = true
            _error.value = null

            try {

                _images.value = repository.getImages()

            } catch (exception: Exception) {

                _error.value =
                    exception.message ?: "Unable to load photos."

            } finally {

                _isLoading.value = false
            }
        }
    }
}