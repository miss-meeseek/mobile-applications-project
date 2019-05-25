package com.example.imagegallery

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.io.File

class GalleryFragment : Fragment(){

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.d("IMGd (onActivityResult)", "done")

        Log.d("IMGd MAIN (onCreate)", Environment.getExternalStorageDirectory().toString())
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_layout, container, false)

        return view
    }

}