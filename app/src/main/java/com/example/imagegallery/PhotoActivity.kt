package com.example.imagegallery

import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_photo.*
import kotlinx.android.synthetic.main.image_detail.*
import java.io.File

class PhotoActivity : AppCompatActivity() {
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

        val _intent = Intent(this, GalleryActivity::class.java)
        _intent.putExtra("path", com.example.imagegallery.path)
        startActivityForResult(_intent, 2)
    }

    override fun onResume() {
        Log.d("On resume", "   PhotoActivity ...")
        super.onResume()
    }
}
