package com.diffusion4812.receipttracker.ui

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.ui.geometry.Offset
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diffusion4812.receipttracker.data.Receipt
import com.diffusion4812.receipttracker.data.ReceiptRepository
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import kotlin.time.Duration.Companion.seconds

class CameraPreviewViewModel(private val receiptRepository: ReceiptRepository) : ViewModel() {
    // Used to set up a link between the Camera and your UI.
    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest

    private var surfaceMeteringPointFactory: SurfaceOrientedMeteringPointFactory? = null
    private val _imageCaptured = MutableSharedFlow<Uri>()
    val imageCaptured: SharedFlow<Uri> = _imageCaptured.asSharedFlow()

    private var imageCapture: ImageCapture? = null
    private var cameraControl: CameraControl? = null

    private val cameraPreviewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _surfaceRequest.update { newSurfaceRequest }
            surfaceMeteringPointFactory  = SurfaceOrientedMeteringPointFactory(
                newSurfaceRequest.resolution.width.toFloat(),
                newSurfaceRequest.resolution.height.toFloat()
            )
        }
    }
    private val imageCaptureUseCase = ImageCapture.Builder().build().also { imageCapture = it }

    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
        val camera = processCameraProvider.bindToLifecycle(
            lifecycleOwner, DEFAULT_BACK_CAMERA, cameraPreviewUseCase, imageCaptureUseCase
        )
        cameraControl = camera.cameraControl

        // Cancellation signals we're done with the camera
        try { awaitCancellation() } finally {
            processCameraProvider.unbindAll()
            cameraControl = null
        }
    }

    @SuppressLint("RestrictedApi")
    private fun _takePhoto(appContext: Context, onImageSavedCallback: ImageCapture.OnImageSavedCallback, resolver: ContentResolver) {
       // Create a unique file name for the image
       val fileName = "receipt_${System.currentTimeMillis()}.jpg"
        // Save to app directory
        val imageDir = File(appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ReceiptTracker")
        if (!imageDir.exists()) {
            imageDir.mkdirs()
        }
        val file = File(imageDir, fileName)
        val outputOptions = OutputFileOptions.Builder(file).build()

       Log.d("CameraPreviewViewModel", "Before taking photo, check directory exists:${imageDir.exists()}")
       imageCapture?.takePicture(outputOptions, ContextCompat.getMainExecutor(appContext), onImageSavedCallback)
    }

    fun takePhoto(
        onNavigateToHomeScreen : () ->Unit,
        appContext: Context
    ){
        val resolver = appContext.contentResolver
        val outputDir = appContext.getExternalFilesDir(null)
        Log.d("CameraPreviewViewModel", "opening photo check dir ${outputDir?.exists()}")
        Log.d("CameraPreviewViewModel", "opening photo check length ${outputDir?.listFiles()?.size}")

        val onImageSavedCallback = object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = outputFileResults.savedUri
                val outputDirectory = appContext.getExternalFilesDir(null)?.absolutePath ?: ""

                val message = "Image saved to: ${savedUri?.toString() ?: "Unknown URI"}, location: $outputDirectory"
                viewModelScope.launch {
                    Log.d("CameraPreviewViewModel", message)

                    delay(5.seconds)

                    outputFileResults.savedUri?.let { uri ->
                        _imageCaptured.tryEmit(uri)
                        val receipt = Receipt(
                            claimId = 0,
                            receiptDate = System.currentTimeMillis(),
                            receiptDescription = "Receipt",
                            receiptAmount = 0.0,
                            imagePath = uri.toString()
                        )
                        receiptRepository.insert(receipt)
                    }
                }.also { onNavigateToHomeScreen() }
            }
            override fun onError(exception: androidx.camera.core.ImageCaptureException) {}
        }

        _takePhoto(appContext, onImageSavedCallback, resolver)
    }

    fun tapToFocus(tapCoords: Offset) {
        val point = surfaceMeteringPointFactory?.createPoint(tapCoords.x, tapCoords.y)
        if (point != null) {
            val meteringAction = FocusMeteringAction.Builder(point).build()
            cameraControl?.startFocusAndMetering(meteringAction)
        }
    }
}