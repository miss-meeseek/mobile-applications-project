package com.example.imagegallery

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

var adapter : ViewPagerAdapter? = null

var TAKE_PHOTO_REQUEST_CODE = 2

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        if (viewPager != null) {
            adapter = ViewPagerAdapter(supportFragmentManager)
            viewPager.adapter = adapter
        }

    }

    override fun onBackPressed() {
        val _intent = Intent(this, MainActivity::class.java)
        startActivity(_intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.camera, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        Log.d("IMGd CAMERA (onOptionsItemSelected)", "PRESSED")
        val _intent = Intent(this, MainActivity::class.java)
        startActivity(_intent)

        return true
    }


}