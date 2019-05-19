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
import android.widget.Toast


class GalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_layout)

        if (intent!!.hasExtra("path")) {
            path = intent?.getStringExtra("path").toString()
            Log.d("IMGd MAIN (onActivityResult)", "")
            AsyncTask.execute {
                GalleryDatabase.getInstance(this).userDao().insertAll(Image(0, File(path).name, ""))
            }
            (supportFragmentManager.findFragmentById(R.id.titles))
        }
        (supportFragmentManager.findFragmentById(R.id.titles) as ListFragment).notifyDataInserted()
        Log.d("IMGd (onActivityResult)", "done")
        Toast.makeText(this,"dodano",Toast.LENGTH_LONG).show()

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
            val _intent = Intent(this, CameraActivity::class.java)
            startActivityForResult(_intent, 2)        }
        return true
    }
}
