package online.ship10x.bevi_bestview

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageProcessingViewModel @Inject constructor(
    private val imageProcessor: ImageProcessor
) : ViewModel() {

}