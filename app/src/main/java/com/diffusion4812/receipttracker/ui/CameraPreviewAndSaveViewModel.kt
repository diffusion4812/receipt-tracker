package com.diffusion4812.receipttracker.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.net.Uri
import android.util.Log
import androidx.camera.core.Camera
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.ui.geometry.Offset
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diffusion4812.receipttracker.data.Receipt
import com.diffusion4812.receipttracker.data.ReceiptRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URLEncoder
import java.nio.ByteBuffer
import java.util.concurrent.Executor
import kotlin.time.Duration.Companion.seconds

class CameraPreviewAndSaveViewModel(private val receiptRepository: ReceiptRepository) : ViewModel() {
    // Used to set up a link between the Camera and your UI.
    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest

    private var surfaceMeteringPointFactory: SurfaceOrientedMeteringPointFactory? = null
    private val _imageCaptured = MutableSharedFlow<Uri>()
    val imageCaptured: SharedFlow<Uri> = _imageCaptured.asSharedFlow()

    private var _imageBytes = MutableStateFlow<ByteArray?>(null)
    val imageBytes : StateFlow<ByteArray?> = _imageBytes

    private var camera : Camera? = null
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

    suspend fun bindToCamera(
        appContext: Context,
        lifecycleOwner: LifecycleOwner
    ) {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
        camera = processCameraProvider.bindToLifecycle(
            lifecycleOwner, DEFAULT_BACK_CAMERA, cameraPreviewUseCase, imageCaptureUseCase
        )
        cameraControl = camera?.cameraControl

        // Cancellation signals we're done with the camera
        try { awaitCancellation() } finally {
            processCameraProvider.unbindAll()
            cameraControl = null
        }
    }

    private fun imageProxyToByteArray(image: ImageProxy): ByteArray {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return bytes
    }

    fun takePhotoAndSave(
        appContext: Context,
        onNavigateToPicturePreviewScreen: (String) -> Unit
    ) {
        val mainExecutor = ContextCompat.getMainExecutor(appContext)
        imageCapture?.takePicture(
            mainExecutor,
            object : ImageCapture.OnImageCapturedCallback() {

                override fun onCaptureSuccess(image: ImageProxy) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val byteArray = imageProxyToByteArray(image)

                            val outputDir = appContext.getExternalFilesDir(null)
                            val fileName = "receipt_${System.currentTimeMillis()}.jpg"
                            val file = File(outputDir, fileName)
                            file.writeBytes(byteArray)

                            val receipt = Receipt(
                                claimId = 0,
                                receiptDate = System.currentTimeMillis(),
                                receiptDescription = "Receipt",
                                receiptAmount = 0.0,
                                imagePath = file.absolutePath
                            )

                            val receiptJson = URLEncoder.encode(Json.encodeToString(receipt), "UTF-8")

                            withContext(Dispatchers.Main) {
                                onNavigateToPicturePreviewScreen(receiptJson)
                            }
                        } catch (e: Exception) {
                            Log.e("CameraPreviewViewModel", "Error saving photo: ${e.message}")
                        } finally {
                            image.close()
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraPreviewViewModel", "Error taking photo: ${exception.message}")
                }
            }
        )
    }

    fun tapToFocus(tapCoords: Offset) {
        val point = surfaceMeteringPointFactory?.createPoint(tapCoords.x, tapCoords.y)
        if (point != null) {
            val meteringAction = FocusMeteringAction.Builder(point).build()
            cameraControl?.startFocusAndMetering(meteringAction)
        }
    }
}