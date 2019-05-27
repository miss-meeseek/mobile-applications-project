package com.example.imagegallery

<<<<<<< HEAD
import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings
import android.util.Base64
import android.util.Base64.DEFAULT
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_photo.*

import java.io.ByteArrayOutputStream
import java.io.File

const val UPDATE_INTERVAL = (10 * 1000).toLong()
const val FASTEST_INTERVAL: Long = 10000

class PhotoActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
=======
import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_photo.*
import kotlinx.android.synthetic.main.fragment_layout.*
import java.io.File



class PhotoActivity : AppCompatActivity() {
>>>>>>> de38ca717af24345259d541be5b6b68312f3f30a

    var TAKE_PHOTO_REQUEST_CODE = 2
    var path = ""

<<<<<<< HEAD
    private var lat: Double? = null
    private var lng: Double? = null
    private var mLocation: Location? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var locationManager: LocationManager? = null
    private var mLocationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        ActivityCompat.requestPermissions(
            this, arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION), 1)

=======
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
>>>>>>> de38ca717af24345259d541be5b6b68312f3f30a
        path = intent.getStringExtra("path")
        Log.d("IMGd PHOTO (onCreate)", path.toString())
        Glide.with(this.applicationContext).load(path).centerCrop().placeholder(R.drawable.ic_launcher_foreground).into(desc_img)

<<<<<<< HEAD
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        checkLocation()
    }

    fun onSend(view: View) {
        Log.d("IMGd PHOTO (onSend)", edit_desc.text.toString())
        Log.d("Path: ", path)

        Log.d("Testmap Lat: ", lat.toString())
        Log.d("Testmap Lng: ", lng.toString())

        if (path != "") {
            AsyncTask.execute {
                GalleryDatabase.getInstance(this).userDao().insertAll(Image(0, File(path).name, lat.toString(), lng.toString()))
            }
            (supportFragmentManager.findFragmentById(R.id.titles))
        }
=======
    }

    public fun onSend(view: View) {
        Log.d("IMGd PHOTO (onSend)", edit_desc.text.toString())
        Log.d("Path: ", path)



        // add photo
        if (path != "") {
            AsyncTask.execute {
                GalleryDatabase.getInstance(this).userDao().insertAll(Image(0, File(path).name))
            }
            (supportFragmentManager.findFragmentById(R.id.titles))
        }
        //(supportFragmentManager!!.findFragmentById(R.id.titles) as ListFragment).notifyDataInserted()
>>>>>>> de38ca717af24345259d541be5b6b68312f3f30a

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

<<<<<<< HEAD
=======



>>>>>>> de38ca717af24345259d541be5b6b68312f3f30a
        // back to MainActivity
        val _intent = Intent(this, MainActivity::class.java)
        _intent.putExtra("path", com.example.imagegallery.path)
        startActivityForResult(_intent, TAKE_PHOTO_REQUEST_CODE)
    }

    override fun onResume() {
        Log.d("On resume", "   PhotoActivity ...")
        super.onResume()
    }
<<<<<<< HEAD
    private val isLocationEnabled: Boolean
        get() {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }
    override fun onConnected(p0: Bundle?) {
        if(ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) Log.d("Testmap", "1 is ok")
        if(ActivityCompat.checkSelfPermission(
                this,
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) Log.d("Testmap", "2 is ok")

        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Testmap", "onConnected problem")
            return
        }

        startLocationUpdates()

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)

        if (mLocation == null) {
            startLocationUpdates()
        }
        if (mLocation != null) {

        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onConnectionSuspended(i: Int) {
        Log.i("Testmap", "Connection Suspended")
        mGoogleApiClient!!.connect()
    }
    override fun onConnectionFailed(connectionResult: ConnectionResult) {
    }

    private fun startLocationUpdates() {
        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL)
            .setFastestInterval(FASTEST_INTERVAL)
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Testmap", "startLocationUpdates problem")
            return
        }
        Log.d("Testmap", "startLocationUpdates is ok")
        LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient,
            mLocationRequest, this
        )
    }
    override fun onLocationChanged(location: Location) {
        Log.d("Testmap lat", location.latitude.toString())
        Log.d("Testmap lng", location.longitude.toString())
        lat = location.latitude
        lng = location.longitude
    }

    private fun checkLocation(): Boolean {
        if (!isLocationEnabled)
            showAlert()
        else Log.d("Testmap", "location is ok")
        return isLocationEnabled
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
            .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
            .setPositiveButton("Location Settings") { _, _ ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }
            .setNegativeButton("Cancel") { _, _ -> }
        dialog.show()
    }
    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) {
            mGoogleApiClient!!.connect()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.disconnect()
        }
    }
=======
>>>>>>> de38ca717af24345259d541be5b6b68312f3f30a
}
