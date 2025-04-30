package com.santeut.data.util

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class CameraXImpl : CameraX {

    private val _facing = MutableStateFlow(CameraSelector.LENS_FACING_BACK)
    private val _flash = MutableStateFlow(false)
    private val _recordingState = MutableStateFlow<RecordingState>(RecordingState.Idle)
    private val _recordingInfo = MutableSharedFlow<RecordingInfo>()

    private lateinit var previewView: PreviewView
    private lateinit var preview: Preview
    private lateinit var cameraProvider: ListenableFuture<ProcessCameraProvider>
    private lateinit var provider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var context: Context
    private lateinit var executor : ExecutorService
    private lateinit var recording: Recording
    private lateinit var mediaStoreOutput: MediaStoreOutputOptions

    private lateinit var imageCapture : ImageCapture
    private lateinit var videoCapture: VideoCapture<Recorder>
    override fun initialize(context: Context) {
        previewView = PreviewView(context)
        preview = Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }
        cameraProvider = ProcessCameraProvider.getInstance(context)
        provider = cameraProvider.get()
        imageCapture = ImageCapture.Builder().build()
        executor = Executors.newSingleThreadExecutor()
        this.context = context
        initializeVideo()
    }

    @OptIn(ExperimentalCamera2Interop::class)
    fun initializeVideo(){
        val qualitySelector = QualitySelector.fromOrderedList(
            listOf(Quality.UHD, Quality.FHD, Quality.HD, Quality.SD),
            FallbackStrategy.lowerQualityOrHigherThan(Quality.SD))

        val recorder = Recorder.Builder()
            .setExecutor(executor).setQualitySelector(qualitySelector)
            .build()
        videoCapture = VideoCapture.withOutput(recorder)

        val path =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/cameraX")
        if (!path.exists()) path.mkdirs();
        val name = "" + SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss-SSS", Locale.KOREA
        ).format(System.currentTimeMillis()) + ".mp4"

        mediaStoreOutput = MediaStoreOutputOptions.Builder(context.contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(ContentValues().apply {
                put(MediaStore.Video.Media.DISPLAY_NAME, name)
            })
            .build()
    }

    override fun startCamera(
        lifecycleOwner: LifecycleOwner,
    ) {
        unBindCamera()

        val cameraSelector = CameraSelector.Builder().requireLensFacing(_facing.value).build()

        cameraProvider.addListener({
            CoroutineScope(Dispatchers.Main).launch {
                provider = cameraProvider.get()
                camera = provider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture,
                    videoCapture
                )
            }
        }, ContextCompat.getMainExecutor(context))
    }

    override fun takePicture( showMessage: (String) -> Unit
    ) {
        Log.d("Take Picture Start", "Take Picture 메서드 들어옴")
        val path =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/cameraX")
        if (!path.exists()) path.mkdirs();
        val photoFile = File(
            path, SimpleDateFormat(
                "yyyy-MM-dd-HH-mm-ss-SSS", Locale.KOREA
            ).format(System.currentTimeMillis()) + ".jpg"
        )
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        CoroutineScope(Dispatchers.Default + SupervisorJob()).launch {
            imageCapture.takePicture(outputFileOptions,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(error: ImageCaptureException) {
                        Log.d("Error", "${error.printStackTrace()}")
                    }

                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        Log.d("Success", "TakePicture Success")
                        showMessage(
                            "Capture Success!! Image Saved at  \n [${Environment.getExternalStorageDirectory().absolutePath}/${Environment.DIRECTORY_PICTURES}/cameraX]"
                        )
                    }
                })
        }
    }

    override fun flipCameraFacing() {
        TODO("Not yet implemented")
    }

    override fun turnOnOffFlash() {
        TODO("Not yet implemented")
    }

    override fun unBindCamera() {
        cameraProvider.get().unbindAll()
    }

    override fun getPreviewView(): PreviewView = previewView
    override fun getFacingState(): StateFlow<Int> = _facing.asStateFlow()

}