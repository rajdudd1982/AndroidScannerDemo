package com.scanner.demo.helpers

import android.content.Context
import android.net.Uri
import androidx.camera.core.*
import androidx.camera.extensions.HdrImageCaptureExtender
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class CameraXHelper() : ViewModel() {

    private val executor: Executor = Executors.newSingleThreadExecutor()
    var imageCaptureLiveData: MutableLiveData<ImageCapture> = MutableLiveData()
    private lateinit var camera: Camera
    var savedImageUriLiveData: MutableLiveData<Uri>  = MutableLiveData()


    fun startCamera(activity: Context, previewView: PreviewView)  {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)
        cameraProviderFuture.addListener(Runnable {
            try {
                val cameraProvider = cameraProviderFuture.get()
                bindPreview(activity, cameraProvider, previewView)
            } catch (e: ExecutionException) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            } catch (e: InterruptedException) {
            }
        }, ContextCompat.getMainExecutor(activity))
    }

    private fun bindPreview(activity: Context, cameraProvider: ProcessCameraProvider, previewView: PreviewView)  {
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        val imageAnalysis = ImageAnalysis.Builder().build()
        val builder = ImageCapture.Builder()

        //Vendor-Extensions (The CameraX extensions dependency in build.gradle)
//        val hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder)
//
//        // Query if extension is available (optional).
//        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
//            // Enable the extension if available.
//            hdrImageCaptureExtender.enableExtension(cameraSelector)
//        }

        val imageCapture = builder.setTargetRotation(activity.resources.configuration.orientation).build()
        preview.setSurfaceProvider(previewView.surfaceProvider)

        camera = cameraProvider.bindToLifecycle((activity as LifecycleOwner), cameraSelector, preview, imageAnalysis, imageCapture)
        imageCaptureLiveData.value = imageCapture
    }

    fun takePicture(file: File) {
        val outputFileOptions: ImageCapture.OutputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()
        imageCaptureLiveData.value?.takePicture(outputFileOptions, executor, object: ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                savedImageUriLiveData.postValue(outputFileResults.savedUri)
            }

            override fun onError(exception: ImageCaptureException) {
                savedImageUriLiveData.postValue(null)
            }
        })
    }
}