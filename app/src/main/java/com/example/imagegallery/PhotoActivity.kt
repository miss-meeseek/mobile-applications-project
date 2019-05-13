package com.example.imagegallery

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_photo.*
import kotlinx.android.synthetic.main.image_detail.*

class PhotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        val path = intent.getStringExtra("path")
        Log.d("IMGd PHOTO (onCreate)", path.toString())
        Glide.with(this.applicationContext).load(path).centerCrop().placeholder(R.drawable.ic_launcher_foreground).into(desc_img)

    }

    public fun onSend(view: View) {
        val intent = Intent()
        Log.d("IMGd PHOTO (onSend)", edit_desc.text.toString())
        intent.putExtra("desc", edit_desc.text.toString())
        setResult(2, intent)
        finish()
    }

}
