package com.example.imagegallery

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cell_layout.view.*

public class ImageViewHolder(
    val view: View
) :
    RecyclerView.ViewHolder(view) {

    var viewImg = view.image_small
    var viewRating = view.ratingBar

    fun setText() {

    }
}
