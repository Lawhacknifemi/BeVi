package online.ship10x.bevi_bestview

class ObjectDetectorListener(
    val onErrorCallback: (error: String, errorCode: Int) -> Unit,
    val onResultsCallback: (resultBundle: ObjectDetectorHelper.ResultBundle) -> Unit
) : ObjectDetectorHelper.DetectorListener {

    override fun onError(error: String, errorCode: Int) {
        onErrorCallback(error, errorCode)
    }

    override fun onResults(resultBundle: ObjectDetectorHelper.ResultBundle) {
        onResultsCallback(resultBundle)
    }
}