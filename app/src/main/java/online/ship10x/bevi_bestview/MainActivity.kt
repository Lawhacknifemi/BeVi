package online.ship10x.bevi_bestview

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import online.ship10x.bevi_bestview.composables.CameraScreen
import online.ship10x.bevi_bestview.composables.ImageProcessingScreen
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

    companion object {
        private const val REQUEST_CODE_PICK_IMAGES = 100
    }

    private fun openLatestImage() {
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val contentUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
                // Handle the latest image URI
                handleSelectedImage(contentUri)
            }
        }
    }

    private fun handleSelectedImage(uri: Uri) {
        imageProcessingViewModel.setSelectedImageUri(uri)
        navController.navigate("imageProcessing")
    }

    private fun openLatestImages() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(Intent.createChooser(intent, "Select Recent Images"), REQUEST_CODE_PICK_IMAGES)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
        enableEdgeToEdge()
        setContent {
            BeViBestViewTheme {
                var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

                navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }

    @Composable
    fun AppNavigation(navController: NavHostController) {
        NavHost(navController = navController, startDestination = "main") {
            composable("main") {
                MainScreen(
                    onOpenCamera = { checkAndRequestCameraPermission() },
                    onImageSelected = { uri -> handleSelectedImage(uri) },
                    onOpenLatest = { openLatestImage() }
                )
            }
            composable("camera") {
                CameraScreen(
                    outputDirectory = outputDirectory,
                    executor = cameraExecutor,
                    onImageCaptured = { /* Handle captured image */ },
                    onError = { /* Handle error */ },
                    onBackPressed = { navController.popBackStack() }
                )
            }
            composable("imageProcessing") {
                ImageProcessingScreen(
                    viewModel = imageProcessingViewModel,
                    onBackPressed = { navController.popBackStack() }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGES && resultCode == RESULT_OK) {
            // Handle the selected images here
            // You can pass the selected images to your ViewModel or process them as needed
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}



