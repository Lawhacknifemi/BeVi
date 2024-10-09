package online.ship10x.bevi_bestview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageProcessingViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri

    private val _detectionResult = MutableStateFlow<ObjectDetectorHelper.ResultBundle?>(null)
    val detectionResult: StateFlow<ObjectDetectorHelper.ResultBundle?> = _detectionResult

    private lateinit var objectDetectorHelper: ObjectDetectorHelper

    init {
        setupObjectDetector()
    }

    private fun setupObjectDetector() {
        objectDetectorHelper = ObjectDetectorHelper(
            context = context,
            objectDetectorListener = ObjectDetectorListener(
                onErrorCallback = { error, errorCode ->
                    // Handle error
                },
                onResultsCallback = { resultBundle ->
                    _detectionResult.value = resultBundle
                }
            )
        )
    }

    fun setSelectedImageUri(uri: Uri) {
        _selectedImageUri.value = uri
        detectObjects(uri)
    }

    private fun detectObjects(uri: Uri) {
        viewModelScope.launch {
            val bitmap = getBitmapFromUri(uri)
            bitmap?.let {
                val result = objectDetectorHelper.detectImage(it)
                _detectionResult.value = result
            }
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}