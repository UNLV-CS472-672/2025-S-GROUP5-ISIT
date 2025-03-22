package com.example.ingrediscan.Camera
import android.Manifest
import android.content.Context
import android.os.Build

//CamaraX imports
import android.content.ContentValues
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import java.text.SimpleDateFormat
import java.util.Locale

class CameraXFeatures(private val context:Context, private val lifecycleOwner: LifecycleOwner,
                      private val previewView : PreviewView)
{

    private var imageCapture : ImageCapture? = null

    //created a thread so the main thread isnt used
    private var cameraExecutor: ExecutorService =
        Executors.newSingleThreadExecutor()

    // Starts the camera
    fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider :  ProcessCameraProvider = cameraProviderFuture.get()

            //sets up preview this is what makes us see what the camera sees
            val preview = Preview.Builder().build()
                .also{ it.setSurfaceProvider(previewView.surfaceProvider)}

            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                //binds the camera provider to lifecycle
                cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
            }catch(exc: Exception){
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun takePhoto(){
        // returns if the imagecapture is null
        val imageCapture = imageCapture ?: return

        //metadata for the image gives time, type of image...
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply{
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }
        //stores the images to the phone. This will change
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()
        //captures the image
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback{
                //handles error if captured succeeded or failed
                override fun onError(exc: ImageCaptureException){
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }
                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults){
                            val msg = "Photo capture succeeded: ${output.savedUri}"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            Log.d(TAG, msg)
                        }
            }
        )
    }



    //Destroyed the thread once is done
    fun onDestroy() {
        cameraExecutor.shutdown()
    }
    companion object{
        private const val TAG = "Ingredi"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSSS"
        val REQUIRED_PERMISSIONS =
            arrayOf( Manifest.permission.CAMERA)
    }

}