package com.example.imagegallery

enum class Model private constructor(val color: Int,val layoutResId: Int) {
    GALLERY(R.string.one, R.layout.image_list),
    GALLER(R.string.one, R.layout.camera_activity),
    GALER(R.string.one, R.layout.image_list)

}