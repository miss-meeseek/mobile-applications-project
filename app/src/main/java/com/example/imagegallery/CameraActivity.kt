package com.example.imagegallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.*
import io.fotoapparat.view.CameraView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.camera_activity.*
import java.io.File
import java.io.IOException

var fotoapparat: Fotoapparat? = null
var fotoapparatState : FotoapparatState? = null
var cameraStatus : CameraState? = null
var flashState: FlashState? = null
var path = ""
val permissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)

val REQUEST_TAKE_PHOTO = 1
val REQUEST_DESC_PHOTO = 2

class CameraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_activity)
        val REQUEST_DESC_PHOTO = 2

        createFotoapparat()

        cameraStatus = CameraState.BACK
        flashState = FlashState.OFF
        fotoapparatState = FotoapparatState.OFF

        fab_camera.setOnClickListener {
            takePhoto()
        }

        fab_switch_camera.setOnClickListener {
            switchCamera()
        }

        fab_flash.setOnClickListener {
            changeFlashState()
        }
    }

    private fun createFotoapparat() {
        val cameraView = findViewById<CameraView>(R.id.camera_view)

        fotoapparat = Fotoapparat(
            context = this,
            view = cameraView,
            scaleType = ScaleType.CenterCrop,
            lensPosition = back(),
            logger = loggers(
                logcat()
            ),
            cameraErrorCallback = { error ->
                println("Recorder errors: $error")
            }
        )
    }

    private fun changeFlashState() {
        fotoapparat?.updateConfiguration(
            CameraConfiguration(
                flashMode = if (flashState == FlashState.TORCH) off() else torch()
            )
        )

        if (flashState == FlashState.TORCH) flashState = FlashState.OFF
        else flashState = FlashState.TORCH
    }

    private fun switchCamera() {
        fotoapparat?.switchTo(
            lensPosition = if (cameraStatus == CameraState.BACK) front() else back(),
            cameraConfiguration = CameraConfiguration()
        )

        if (cameraStatus == CameraState.BACK) cameraStatus = CameraState.FRONT
        else cameraStatus = CameraState.BACK
    }

    private fun takePhoto() {

        if (hasNoPermissions()) {
            requestPermission()
        } else {
            val photoFile: File? = try {
                val storage = StorageManager.createImageFile(this)
                path = storage.path
                storage

            } catch (ex: IOException) {
                File("/a")
            }
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "com.example.android.fileprovider",
                    it
                )

                fotoapparat
                    ?.takePicture()
                    ?.saveToFile(photoFile!!)

                val _intent = Intent(this, PhotoActivity::class.java)
                _intent.putExtra("path", path)
                startActivityForResult(_intent, REQUEST_TAKE_PHOTO)
            }
        }
    }


override fun onStart() {
    super.onStart()
    if (hasNoPermissions()) {
        requestPermission()
    }else{
        fotoapparat?.start()
        fotoapparatState = FotoapparatState.ON
    }
}

private fun hasNoPermissions(): Boolean{
    return ContextCompat.checkSelfPermission(this,
        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
}

fun requestPermission(){
    ActivityCompat.requestPermissions(this, permissions,0)
}

override fun onStop() {
    super.onStop()
    fotoapparat?.stop()
    FotoapparatState.OFF
}

override fun onResume() {
    super.onResume()
    if(!hasNoPermissions() && fotoapparatState == FotoapparatState.OFF){
        val intent = Intent(baseContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

}

enum class CameraState{
    FRONT, BACK
}

enum class FlashState{
    TORCH, OFF
}

enum class FotoapparatState{
    ON, OFF
}