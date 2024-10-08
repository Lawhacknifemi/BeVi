package online.ship10x.bevi_bestview

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import online.ship10x.bevi_bestview.composables.BeviBottomSheet
import online.ship10x.bevi_bestview.composables.MainScreen
import online.ship10x.bevi_bestview.ui.theme.BeViBestViewTheme
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val imageProcessingViewModel: ImageProcessingViewModel by viewModels()
    private lateinit var cameraExecutor: ExecutorService  // Changed from Executor to ExecutorService

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            // Handle permission denied
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
        enableEdgeToEdge()
        setContent {
            BeViBestViewTheme {
                MainScreen(::checkAndRequestCameraPermission)
            }
        }
    }

    private fun checkAndRequestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        val intent = android.content.Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}



//@Composable
//fun CameraPreview(modifier: Modifier = Modifier) {
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val context = LocalContext.current
//    var previewUseCase by remember { mutableStateOf<androidx.camera.core.Preview?>(null) }
//    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
//
//    LaunchedEffect(Unit) {
//        cameraProvider = context.getCameraProvider()
//    }
//
//    AndroidView(
//        modifier = modifier,
//        factory = { ctx ->
//            val previewView = PreviewView(ctx).apply {
//                this.scaleType = PreviewView.ScaleType.FILL_CENTER
//            }
//            // CameraX Preview UseCase
//            previewUseCase = androidx.camera.core.Preview.Builder()
//                .build()
//                .also {
//                    it.setSurfaceProvider(previewView.surfaceProvider)
//                }
//
//            previewView
//        },
//        update = { previewView ->
//            val cameraSelector = CameraSelector.Builder()
//                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//                .build()
//
//            cameraProvider?.let { provider ->
//                try {
//                    // Must unbind the use-cases before rebinding them.
//                    provider.unbindAll()
//                    provider.bindToLifecycle(
//                        lifecycleOwner, cameraSelector, previewUseCase
//                    )
//                } catch (ex: Exception) {
//                    // Log.e("CameraPreview", "Use case binding failed", ex)
//                }
//            }
//        }
//    )
//}
//

