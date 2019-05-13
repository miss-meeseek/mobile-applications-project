package com.example.imagegallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import android.os.StrictMode


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_layout)
        Log.d("IMGd MAIN (onCreate)", Environment.getExternalStorageDirectory().toString())
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.camera, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_camera) {
            Log.d("IMGd CAMERA (onOptionsItemSelected)", "PRESSED")
            dispatchTakePictureIntent()
        }
        return true
    }

    val REQUEST_TAKE_PHOTO = 1
    val REQUEST_DESC_PHOTO = 2
    var path = ""
    var _id = 0

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("IMGd (onActivityResult)", supportFragmentManager.findFragmentById(R.id.titles).toString())
        Log.d("IMGd MAIN (onActivityResult)", requestCode.toString())
        Log.d("IMGd MAIN (onActivityResult)", resultCode.toString())
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            val _intent = Intent(this, PhotoActivity::class.java)
            _intent.putExtra("path", path)
            startActivityForResult(_intent, REQUEST_DESC_PHOTO)
        }
        if (requestCode == REQUEST_DESC_PHOTO && resultCode == 2) {
            Log.d("IMGd MAIN (onActivityResult)", data?.hasExtra("desc").toString())
            if (data!!.hasExtra("desc")) {
                val desc = data?.getStringExtra("desc")
                Log.d("IMGd MAIN (onActivityResult)", desc)
                AsyncTask.execute {
                    GalleryDatabase.getInstance(this).userDao().insertAll(Image(0, File(path).name, desc, 0.0f, 0))
                }
                    (supportFragmentManager.findFragmentById(R.id.titles))
            }
            (supportFragmentManager.findFragmentById(R.id.titles) as ListFragment).notifyDataInserted()
            Log.d("IMGd (onActivityResult)", "done")
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    val storage = StorageManager.createImageFile(this)
                    path = storage.path
                    storage

                } catch (ex: IOException) {
                    File("/a")
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    Log.d("IMGd MAIN (dispatchTakePictureIntent)", photoFile.toString())
                    Log.d("IMGd MAIN (dispatchTakePictureIntent)", Uri.fromFile(photoFile).toString())
                    Log.d("IMGd MAIN (dispatchTakePictureIntent)", File(photoURI.path).toString())
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

}
