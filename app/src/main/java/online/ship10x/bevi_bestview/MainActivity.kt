package online.ship10x.bevi_bestview

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import online.ship10x.bevi_bestview.composables.CameraScreen
import online.ship10x.bevi_bestview.composables.MainScreen
import online.ship10x.bevi_bestview.ui.theme.BeViBestViewTheme
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val imageProcessingViewModel: ImageProcessingViewModel by viewModels()
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var navController: NavHostController

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            navigateToCamera()
        } else {
            // Handle permission denied
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
        enableEdgeToEdge()
        setContent {
            BeViBestViewTheme {
                navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }

    @Composable
    fun AppNavigation(navController: NavHostController) {
        NavHost(navController = navController, startDestination = "main") {
            composable("main") {
                MainScreen(onOpenCamera = { checkAndRequestCameraPermission() })
            }
            composable("camera") {
                CameraScreen(
                    outputDirectory = outputDirectory,
                    executor = cameraExecutor,
                    onImageCaptured = { /* Handle captured image */ },
                    onError = { /* Handle error */ }
                )
            }
        }
    }

    private fun checkAndRequestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                navigateToCamera()
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun navigateToCamera() {
        navController.navigate("camera")
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
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

