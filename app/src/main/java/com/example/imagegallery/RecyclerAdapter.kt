package com.example.imagegallery

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SizeReadyCallback


public class RecyclerAdapter(
    val context: Context?
) : RecyclerView.Adapter<ImageViewHolder>() {

    private var selectedPos = RecyclerView.NO_POSITION

    override fun getItemCount(): Int {
        return GalleryDatabase.getInstance(context!!).userDao().count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cell_layout, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ImageViewHolder, i: Int) {
        var path = StorageManager.getPrivateAlbumStorageDir(context!!, "ImageGallery")?.path + "/" +  GalleryDatabase.getInstance(context).userDao().getFilename(i+1)

        Log.d("IMG id", (i+1).toString())
        Log.d("IMG", "got: " + path)
        Log.d("IMG", "set")
        Log.d("IMG", "imageView null? " + viewHolder.viewImg.toString())

        Glide.with(context!!).load(path).centerCrop().placeholder(R.drawable.ic_launcher_foreground).into(viewHolder.viewImg)

    }


}
