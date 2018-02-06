package com.innexiv.scannerapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.innexiv.scannerapp.R
import com.innexiv.scannerapp.adapter.RoutesAdapter
import com.innexiv.scannerapp.data.RouteSite
import kotlinx.android.synthetic.main.activity_route_sites.*
import org.jetbrains.anko.*
import android.content.pm.PackageManager
import android.Manifest.permission
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.support.v4.app.ActivityCompat
import android.support.design.widget.Snackbar
import android.view.View
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.support.annotation.NonNull
import com.innexiv.scannerapp.BuildConfig
import com.innexiv.scannerapp.services.LocationService


class RouteSitesActivity : AppCompatActivity(), AnkoLogger {

    companion object {
        val KEY_ACTIVITY_ID = "keyActivityId"
        val KEY_ROUTE_ID = "keyRouteId"
        val KEY_SITE_ID = "keySiteId"
        val KEY_USER = "keyUserLogin"
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    private val fusedLocationClient : FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private lateinit var location: Location

    private val user by lazy { intent.getStringExtra(MainActivity.KEY_USER) }

    override fun onStart() {
        super.onStart()

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_sites)

        val siteListFromIntent  = intent.getParcelableArrayListExtra<RouteSite>(RoutesActivity.KEY_ROUTE_SITES)

        startService(Intent(this,LocationService::class.java))

        sitesList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            adapter = RoutesAdapter(siteListFromIntent) {


                //if(distFrom(location.latitude, location.longitude, it.latitude!!.toDouble(), it.longitude!!.toDouble())){

                    //toast("I'm close")
                    startActivity<GatewayActivity>(KEY_USER to user,
                            KEY_ACTIVITY_ID to it.activityId,
                            KEY_ROUTE_ID to it.routeId,
                            KEY_SITE_ID to it.siteId)

                //} else { toast("Lat : ${location.longitude} \n Long: ${location.latitude} \n Not close!")}


            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(){
        fusedLocationClient.lastLocation.addOnCompleteListener(this) {
            it.result?.let{
                location = it
            }
        }
    }

    private fun distFrom(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Boolean {
        val earthRadius = 6371.01 //  km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)
        val sindLat = Math.sin(dLat / 2)
        val sindLng = Math.sin(dLng / 2)
        val a = Math.pow(sindLat, 2.0) + Math.pow(sindLng, 2.0) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return (earthRadius * c) < 0.2
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this@RouteSitesActivity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {

            alert(R.string.permission_rationale,android.R.string.ok){
                yesButton { startLocationPermissionRequest() }
            }.show()
            /*snack(R.string.permission_rationale, android.R.string.ok,
                    object : View.OnClickListener() {
                        fun onClick(view: View) {
                            // Request permission
                            startLocationPermissionRequest()
                        }
                    })*/

        } else {

            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> // Permission granted.
                    getLastLocation()
                else -> {
                    // Permission denied.

                    // Notify the user via a SnackBar that they have rejected a core permission for the
                    // app, which makes the Activity useless. In a real app, core permissions would
                    // typically be best requested during a welcome-screen flow.

                    // Additionally, it is important to remember that a permission might have been
                    // rejected without asking the user for permission (device policy or "Never ask
                    // again" prompts). Therefore, a user interface affordance is typically implemented
                    // when permissions are denied. Otherwise, your app could appear unresponsive to
                    // touches or interactions which have required permissions.
                    /*showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                            View.OnClickListener {
                                // Build intent that displays the App settings screen.
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                val uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null)
                                intent.data = uri
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            })*/
                    alert(R.string.permission_denied_explanation, android.R.string.ok){
                        yesButton { // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null)
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent) }
                    }
                }
            }
        }
    }
}
