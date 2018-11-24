package com.razvanpopescu.socialresponsibility

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.Circle


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                text_view.text = "Progress : $i"
                mMap.clear()
                mMap.addCircle(
                    CircleOptions()
                        .center(LatLng(-34.0, 151.0))
                        .radius(seek_bar.progress.toDouble() * 1000)
                        .strokeColor(Color.RED)
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
                Toast.makeText(applicationContext,"start tracking",Toast.LENGTH_SHORT).show()
                mMap.clear()
                mMap.addCircle(
                    CircleOptions()
                        .center(LatLng(-34.0, 151.0))
                        .radius(seek_bar.progress.toDouble() * 1000)
                        .strokeColor(Color.RED)
                )
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
                Toast.makeText(applicationContext,"stop tracking", Toast.LENGTH_SHORT).show()
                mMap.clear()
                mMap.addCircle(
                    CircleOptions()
                        .center(LatLng(-34.0, 151.0))
                        .radius(seek_bar.progress.toDouble() * 1000)
                        .strokeColor(Color.RED)
                )
            }
        })


    }

    fun login(v : View?){
        var myIntent = Intent(this, LoginActivity::class.java)
        startActivity(myIntent)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.addCircle(
            CircleOptions()
                .center(sydney)
                .radius(seek_bar.progress.toDouble() * 1000)
                .strokeColor(Color.RED)
        )
    }

    fun getLocation() {
        var locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        var locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                var latitute = location!!.latitude
                var longitute = location!!.longitude

                Log.i("test", "Latitute: $latitute ; Longitute: $longitute")

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
            }

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
            return
        }
        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> getLocation()
                //Tell to user the need of grant permission
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }

}
