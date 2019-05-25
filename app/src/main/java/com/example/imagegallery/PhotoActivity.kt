package com.example.imagegallery

import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_photo.*
import kotlinx.android.synthetic.main.fragment_layout.*
import java.io.File



class PhotoActivity : AppCompatActivity() {

    var TAKE_PHOTO_REQUEST_CODE = 2
    var path = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        path = intent.getStringExtra("path")
        Log.d("IMGd PHOTO (onCreate)", path.toString())
        Glide.with(this.applicationContext).load(path).centerCrop().placeholder(R.drawable.ic_launcher_foreground).into(desc_img)

    }

    public fun onSend(view: View) {
        Log.d("IMGd PHOTO (onSend)", edit_desc.text.toString())
        Log.d("Path: ", path)



        // add photo
        if (path != "") {
            AsyncTask.execute {
                GalleryDatabase.getInstance(this).userDao().insertAll(Image(0, File(path).name))
            }
            (supportFragmentManager.findFragmentById(R.id.titles))
        }
        //(supportFragmentManager!!.findFragmentById(R.id.titles) as ListFragment).notifyDataInserted()

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())




        // back to MainActivity
        val _intent = Intent(this, MainActivity::class.java)
        _intent.putExtra("path", com.example.imagegallery.path)
        startActivityForResult(_intent, TAKE_PHOTO_REQUEST_CODE)
    }

    override fun onResume() {
        Log.d("On resume", "   PhotoActivity ...")
        super.onResume()
    }
}
