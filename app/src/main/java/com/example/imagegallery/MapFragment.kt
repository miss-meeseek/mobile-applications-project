package com.example.imagegallery

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.StrictMode
import android.util.Base64
import android.util.Base64.DEFAULT
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.camera_activity.*
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import java.io.ByteArrayOutputStream

class MapFragment : Fragment(){

    val TAG = "MapFragment"
    var lat : String = "null"
    var lng : String = "null"
    var size :Int = 0
    var x :Double = 37.0
    var y : Double  = -122.0


    override fun onAttach(context: Context?) {
        Log.d(TAG, "OnAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "OnCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "OnCreateView")
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {mMap->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            mMap.clear() //clear old markers





            size = GalleryDatabase.getInstance(this.context!!).userDao().count()
            Log.d("COUNT", size.toString())

            for(i in 1 until size - 1 ){

                lat = GalleryDatabase.getInstance(this.context!!).userDao().getLat(i)
                lng = GalleryDatabase.getInstance(this.context!!).userDao().getLng(i)
                if(lat != "null" && lng != "null") {
                    x = lat.toDouble()
                    y = lng.toDouble()

                    var path = StorageManager
                        .getPrivateAlbumStorageDir(this.context!!, "ImageGallery")?.path + "/" + GalleryDatabase.getInstance(this.context!!)
                        .userDao()
                        .getFilename(i)

                    Log.d("PATHH", path.toString())

                    var options = BitmapFactory.Options()
                    options.inMutable = true
                    /*var width = options.outWidth
                    if(width > 20)
                        options.inSampleSize = Math.round((width/20).toDouble()).toInt()
                    options.inJustDecodeBounds = false*/

                    var drawableBitmap = BitmapFactory.decodeFile(path, options)

                    /*val stream = ByteArrayOutputStream()
                    drawableBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    var b64Image = Base64.encodeToString(stream.toByteArray(), DEFAULT)*/

                    //img_det.setImageBitmap(drawableBitmap)

                    /*var imageBytes = Base64.decode(path, 0)
                    var image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)*/

                    mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(x, y))
                            .title("photo:$i")
                            .icon(bitmapDescriptorFromVector(activity as Context, R.drawable.spider))
                                //(bitmapDescriptorFromVector(activity as Context, R.drawable.spider))
                            //(BitmapDescriptorFactory.fromBitmap(image)))
                    )
                    //Log.d("FORR", lat.toString())
                    //val path = StorageManager.getPrivateAlbumStorageDir(context!!, "ImageGallery")?.path + "/" +  GalleryDatabase.getInstance(context!!).userDao().getFilename(shownIndex+1)
                }
            }

            val googlePlex = CameraPosition.builder()
                .target(LatLng(x, y))
                .zoom(10f)
                .bearing(0f)
                .tilt(45f)
                .build()
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null)


        }


        var x : List<String> = GalleryDatabase.getInstance(this.context!!).userDao().getLat()
        Log.d("DATALat", x.toString())
        var y : List<String> = GalleryDatabase.getInstance(this.context!!).userDao().getLng()
        Log.d("DATALng", y.toString())

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        return rootView
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap =
            Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "OnActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        Log.d(TAG, "OnStart")

        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "OnResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "OnPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "OnStop")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d(TAG, "OnDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(TAG, "OnDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(TAG, "OnDetach")
        super.onDetach()
    }
}