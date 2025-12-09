package com.diffusion4812.receipttracker.ui

import android.Manifest
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.geometry.takeOrElse
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diffusion4812.receipttracker.R
import com.diffusion4812.receipttracker.ui.navigation.NavigationDestination
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.delay
import java.util.UUID

object CameraPreviewDestination : NavigationDestination {
    override val route = "camera_preview"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPreviewScreen(
    onNavigateToPicturePreviewScreen : (String) -> Unit,
    viewModel: CameraPreviewAndSaveViewModel,
    modifier: Modifier = Modifier
) {
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA
        )
    )
    if (permissionState.allPermissionsGranted) {
        CameraPreviewContent(
            onNavigateToPicturePreviewScreen = onNavigateToPicturePreviewScreen,
            viewModel,
            modifier)
    } else {

        Column(
            modifier = modifier
                .fillMaxSize()
                .wrapContentSize()
                .widthIn(max = 480.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val textToShow = if (permissionState.shouldShowRationale) {
                // User previously denied permission, so explain why it’s important
                "Camera access is required to capture and store receipts. " +
                "Please grant permission to continue using the receipt capture feature."
            } else {
                // First-time request or 'Don't ask again' scenario
                "To capture receipts directly within the app, we require access to your device's camera. " +
                "Please grant permission to enable this functionality."
            }
            Text(textToShow, textAlign = TextAlign.Center)
            Spacer(Modifier.height(16.dp))
            Button(onClick = { permissionState.launchMultiplePermissionRequest() }) {
                Text("Unleash the Camera!")
            }
        }
    }
}

@Composable
fun CameraPreviewContent(
    onNavigateToPicturePreviewScreen: (String) -> Unit,
    viewModel: CameraPreviewAndSaveViewModel,
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val surfaceRequest by viewModel.surfaceRequest.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(lifecycleOwner) {
        viewModel.bindToCamera(context.applicationContext, lifecycleOwner)
    }

    var autofocusRequest by remember { mutableStateOf(UUID.randomUUID() to Offset.Unspecified) }
    val autofocusRequestId = autofocusRequest.first
    val showAutofocusIndicator = autofocusRequest.second.isSpecified
    val autofocusCoords = remember(autofocusRequestId) { autofocusRequest.second }

    if (showAutofocusIndicator) {
        LaunchedEffect(autofocusRequestId) {
            delay(1000)
            autofocusRequest = autofocusRequestId to Offset.Unspecified
        }
    }

    surfaceRequest?.let { request ->
        val coordinateTransformer = remember { MutableCoordinateTransformer() }

        Box(modifier = Modifier.fillMaxSize()) {
            // Camera Preview
            CameraXViewfinder(
                surfaceRequest = request,
                coordinateTransformer = coordinateTransformer,
                modifier = modifier.pointerInput(Unit) {
                    detectTapGestures { tapCoords ->
                        with(coordinateTransformer) {
                            viewModel.tapToFocus(tapCoords.transform())
                        }
                        autofocusRequest = UUID.randomUUID() to tapCoords
                    }
                }
            )

            // Autofocus Indicator
            AnimatedVisibility(
                visible = showAutofocusIndicator,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                val density = LocalDensity.current
                val xDp = with(density) { autofocusCoords.x.toDp() }
                val yDp = with(density) { autofocusCoords.y.toDp() }

                Box(
                    modifier = Modifier
                        .size(48.dp) // fixed size
                        .offset(x = xDp - 24.dp, y = yDp - 24.dp) // center at tap
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
            }

            // Bottom bar
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                BottomAppBar {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                viewModel.takePhotoAndSave(
                                    context.applicationContext,
                                    onNavigateToPicturePreviewScreen
                                )
                            }
                        ) {
                            Text("Take Photo")
                        }
                    }
                }
            }
        }
    }
}