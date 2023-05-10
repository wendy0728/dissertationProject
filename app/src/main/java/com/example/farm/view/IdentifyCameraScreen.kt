package com.example.farm.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.farm.R
import com.example.farm.viewmodel.detection.CAMERA_EXECUTOR
import com.example.farm.viewmodel.detection.CAMERA_DIRECTORY
import com.example.farm.viewmodel.PestViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * IdentifyCameraScreen.kt
 * Written By: Jing Wen Ng
 *
 * In this page user can choose to take a photo from their camera or choose a photo from their gallery.
 * takePhoto(), getCameraProvider(),handleImageCapture() function are adapted from Luna Lou's code at https://github.com/PervasiveComputingGroup/FCIMCS_Mobile
 *
 *
 *@param navController
 *@param pestViewModel
 *@param markerIndex
 *@param fieldId
 */

@Composable
fun IdentifyCameraScreen(
    navController : NavController,
    pestViewModel : PestViewModel,
    markerIndex: String,
    fieldId: String) {

    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()



    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            handleImageCapture(uri = uri, pestViewModel = pestViewModel, index = markerIndex.toInt())
            Log.i("pestViewModel.markers[markerIndex.toInt()].photoUri", pestViewModel.markers[markerIndex.toInt()].photoUri.toString())
            if(pestViewModel.markers[markerIndex.toInt()].photoUri!=null){
                navController.navigate("identify_show_image/${fieldId}/${markerIndex}")
            }
        }
    }

    //ask gallery permission
    val permission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted: Boolean ->
        if (granted) {
            galleryLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter){
        AndroidView({ previewView }, modifier = Modifier
            .fillMaxSize())
        Row( modifier = Modifier
            .background(Color.Black.copy(0.2f))
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom) {

            IconButton(
                onClick = { navController.navigate("identify_map/${fieldId}") })
            {
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = "Back" ,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(bottom=10.dp,top=10.dp),
                    tint = whiteColor
                )
            }

            IconButton(
                onClick = {
                    takePhoto(
                        imageCapture = imageCapture,
                        onImageCaptured = { handleImageCapture(uri = it, pestViewModel = pestViewModel, index = markerIndex.toInt())},
                        onError = {Log.e("app", "Image capture exception: $it")}
                    )


                    if(pestViewModel.markers[markerIndex.toInt()].photoUri!=null){
                        navController.navigate("identify_show_image/${fieldId}/${markerIndex}")
                    }
                },
                content = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_circle_24),
                        contentDescription = "Take picture",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(bottom=10.dp,top=10.dp),
                        tint = whiteColor
                    )
                }
            )

            IconButton(
                onClick = {
                    if(ContextCompat.checkSelfPermission( context, Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                        galleryLauncher.launch("image/*")
                    }
                    else{
                        permission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                })
            {
                Icon(
                    painter = painterResource(R.drawable.baseline_photo_library_24),
                    contentDescription = "Gallery",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(bottom=10.dp,top=10.dp),
                    tint = whiteColor
                )
            }
        }

    }

}

private fun handleImageCapture(uri: Uri, pestViewModel: PestViewModel, index: Int) {
    pestViewModel.markers[index].photoUri = uri
}


private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

fun takePhoto(
    imageCapture: ImageCapture,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val photoFile = File(
        CAMERA_DIRECTORY,
        SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(outputOptions, CAMERA_EXECUTOR, object : ImageCapture.OnImageSavedCallback {
        override fun onError(exception: ImageCaptureException) {
            Log.e("app", "Take photo error:", exception)
            onError(exception)
        }

        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            val savedUri = Uri.fromFile(photoFile)
            //on save
            onImageCaptured(savedUri)
        }
    })
}


