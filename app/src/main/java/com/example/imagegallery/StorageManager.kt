package com.example.imagegallery

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class StorageManager {

    companion object {

        fun createFile(context: Context?) {
            val filename = "myfile"
            val fileContents = "1,10,4"
            context?.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it?.write(fileContents.toByteArray())
            }

        }

        @Throws(IOException::class)
        fun createImageFile(context: Context): File {
            // Create an image file name
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                (getPrivateAlbumStorageDir(context, "ImageGallery")) /* directory */
            )
        }

        fun getPath(context: Context?): String? {
            return context?.filesDir?.path
        }

        fun openFile(context: Context?, filename: String) {
            val path = context?.filesDir?.path + "/" + filename
            var file = File(path)

            Log.d("READIN", file.readLines().toString())
        }

        fun getPrivateAlbumStorageDir(context: Context, albumName: String): File? {
            // Get the directory for the app's private pictures directory.
            val file = File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName)
            if (!file?.mkdirs()) {
                Log.e("PhotoPIC", "Directory not created")
            }
            return file
        }

    }

}