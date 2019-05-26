package com.example.imagegallery

import android.graphics.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.image_draw.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.io.FileOutputStream
import java.io.OutputStream
import android.view.MotionEvent
import android.widget.*


class DrawFragment : Fragment(), View.OnClickListener,AdapterView.OnItemSelectedListener {


    private var mPaint = Paint()
    private var mPath = Path()

    private var mCurX = 0f
    private var mCurY = 0f
    private var mStartX = 0f
    private var mStartY = 0f




    var spinner: Spinner? = null


    val shownIndex: Int by lazy {
        arguments?.getInt("index", 0) ?: 0
    }

    private var dualPane: Boolean = false

    var curRating = 0.0f

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val detailsFrame: View? = activity?.findViewById(R.id.details)
        dualPane = detailsFrame?.visibility == View.VISIBLE

        curRating = savedInstanceState?.getFloat("curRating", 0.0f) ?: 0.0f
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat("curRating", curRating)
    }

    var colors = arrayOf("Black", "Red", "Blue", "Yellow", "Green", "White")

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        when(position){
            0 -> mPaint.color = Color.BLACK
            1 -> mPaint.color = Color.RED
            2 -> mPaint.color = Color.BLUE
            3 -> mPaint.color = Color.YELLOW
            4 -> mPaint.color = Color.GREEN
            5 -> mPaint.color = Color.WHITE
            else -> mPaint.color = Color.BLACK
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container == null) {
            return null
        }

        val view: View = inflater.inflate(R.layout.image_draw, container, false)
        val btn: Button = view.findViewById(R.id.save)
        btn.setOnClickListener(this)

        spinner = view.findViewById(R.id.spinner_sample)
        spinner!!.onItemSelectedListener = this

        // Create an ArrayAdapter using a simple spinner layout and colors array
        val aa = ArrayAdapter(this.context!!, android.R.layout.simple_spinner_item, colors)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner!!.adapter = aa

        return view
    }

    private lateinit var drawableBitmap: Bitmap

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.save -> {
                savePhoto(drawableBitmap,img_det)
            }

            else -> {
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mPaint.apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 8f
            isAntiAlias = true
        }


        super.onViewCreated(view, savedInstanceState)

        val path = StorageManager
            .getPrivateAlbumStorageDir(context!!, "ImageGallery")?.path + "/" +  GalleryDatabase.getInstance(context!!)
            .userDao()
            .getFilename(shownIndex+1)


        val options = BitmapFactory.Options()
        options.inMutable = true
        val width = options.outWidth
        if (width > img_det.layoutParams.width)
            options.inSampleSize = Math.round((width /  img_det.layoutParams.width).toDouble()).toInt()
        options.inJustDecodeBounds = false

        drawableBitmap = BitmapFactory.decodeFile(path, options).copy(Bitmap.Config.ARGB_8888, true)

        img_det.setImageBitmap(drawableBitmap)

        //globalBitmap = drawableBitmap
        val canvas =  Canvas(drawableBitmap)


        view.setOnTouchListener { v, event ->

            val x = event.x
            val y = event.y - 400

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mStartX = x
                    mStartY = y
                    actionDown(x, y)
                }
                MotionEvent.ACTION_MOVE -> actionMove(x, y)
                MotionEvent.ACTION_UP -> actionUp()

            }
            canvas.drawPath(mPath, mPaint)
            v.invalidate()
            return@setOnTouchListener true
        }

        //savePhoto(drawableBitmap, img_det)


    }

    fun savePhoto(drawableBitmap: Bitmap, img_det: ImageView) {
        val thePath =  saveImageToExternalStorage(drawableBitmap)
        val uri: Uri = Uri.parse(thePath)
        img_det.setImageURI(uri)

        AsyncTask.execute {
            GalleryDatabase.getInstance(this.context!!).userDao().insertAll(Image(0, File(thePath!!).name))
        }
    }
    private fun saveImageToExternalStorage(bitmap:Bitmap): String? {
        Toast.makeText(activity,"Imaged saved!",Toast.LENGTH_SHORT).show()
        //        val uri: Uri = saveImageToExternalStorage(drawableBitmap)
        //        img_det.setImageURI(uri)

        // Get the external storage directory path
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        // Create a file to save the image
        val file = File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            (StorageManager.getPrivateAlbumStorageDir(activity!!.applicationContext, "ImageGallery")) /* directory */
        )

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the output stream
            stream.flush()

            // Close the output stream
            stream.close()
            Log.i("photoee", file.absolutePath)
        } catch (e: IOException){ // Catch the exception
            e.printStackTrace()
        }

        // Return the saved image path to uri
        return file.absolutePath
    }



    private fun actionDown(x: Float, y: Float) {
        mPath.moveTo(x, y)
        mCurX = x
        mCurY = y
    }

    private fun actionMove(x: Float, y: Float) {
        mPath.quadTo(mCurX, mCurY, (x + mCurX) / 2, (y + mCurY) / 2)
        mCurX = x
        mCurY = y
    }

    private fun actionUp() {
        mPath.lineTo(mCurX, mCurY)

        // draw a dot on click
        if (mStartX == mCurX && mStartY == mCurY) {
            mPath.lineTo(mCurX, mCurY + 2)
            mPath.lineTo(mCurX + 1, mCurY + 2)
            mPath.lineTo(mCurX + 1, mCurY)
        }
    }

    companion object {
        fun newInstance(index: Int): DrawFragment {
            val f = DrawFragment()

            // Supply index input as an argument.
            val args = Bundle()
            args.putInt("index", index)
            f.arguments = args
            return f
        }
    }

}