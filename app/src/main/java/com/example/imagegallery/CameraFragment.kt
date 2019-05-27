package com.example.imagegallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
<<<<<<< HEAD

=======
>>>>>>> de38ca717af24345259d541be5b6b68312f3f30a
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import io.fotoapparat.selector.front
import io.fotoapparat.selector.off
import io.fotoapparat.selector.torch
import io.fotoapparat.view.CameraView
import kotlinx.android.synthetic.main.camera_activity.*
import java.io.File
import java.io.IOException

<<<<<<< HEAD



=======
>>>>>>> de38ca717af24345259d541be5b6b68312f3f30a
class CameraFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.camera_activity, container, false)

        return view
    }


    private fun createFotoapparat() {
        val cameraView = activity?.findViewById<CameraView>(R.id.camera_view)

        fotoapparat = cameraView?.let {
            Fotoapparat(
                context = this.context!!,
                view = it,
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
    }
<<<<<<< HEAD
//    private fun openMap(){
//        addFragment(MapFragment(), false, "one")
//    }
//    fun addFragment(fragment: Fragment, addToBackStack: Boolean, tag: String){
//        val manager = fragmentManager
//        val ft = manager!!.beginTransaction()
//
//        if(addToBackStack){
//            ft.addToBackStack(tag)
//        }
//        ft.replace(R.id.container_frame_back, fragment, tag)
//        ft.commitAllowingStateLoss()
//    }
=======

>>>>>>> de38ca717af24345259d541be5b6b68312f3f30a
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
<<<<<<< HEAD
=======

>>>>>>> de38ca717af24345259d541be5b6b68312f3f30a
        if (hasNoPermissions()) {
            requestPermission()
        } else {
            val photoFile: File? = try {
                val storage = StorageManager.createImageFile(this.context!!)
                path = storage.path
                storage

            } catch (ex: IOException) {
                File("/a")
            }
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this.context!!,
                    "com.example.android.fileprovider",
                    it
                )

                fotoapparat
                    ?.takePicture()
                    ?.saveToFile(photoFile!!)

                val intent = Intent().apply {
                    setClass(activity, PhotoActivity::class.java)
                    putExtra("path", path)
                }
                startActivityForResult(intent, REQUEST_TAKE_PHOTO)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        if (hasNoPermissions()) {
            requestPermission()
        } else {
            fotoapparat?.start()
            fotoapparatState = FotoapparatState.ON
        }
    }

    private fun hasNoPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this.context!!,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            this.context!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            this.context!!,
            Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(this.activity!!, permissions, 0)
    }

    override fun onStop() {
        super.onStop()
        fotoapparat?.stop()
        FotoapparatState.OFF
    }

    override fun onResume() {

        super.onResume()
        if (!hasNoPermissions() && fotoapparatState == FotoapparatState.OFF) {
            val intent = Intent().apply {
                setClass(activity, MainActivity::class.java)
            }
            startActivityForResult(intent, 3)
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
