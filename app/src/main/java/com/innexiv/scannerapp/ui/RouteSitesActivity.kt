package com.innexiv.scannerapp.ui


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.innexiv.scannerapp.R
import com.innexiv.scannerapp.adapter.RoutesAdapter
import com.innexiv.scannerapp.data.RouteSite
import kotlinx.android.synthetic.main.activity_route_sites.*
import org.jetbrains.anko.*




class RouteSitesActivity : AppCompatActivity(), AnkoLogger {

    companion object {
        val KEY_ACTIVITY_ID = "keyActivityId"
        val KEY_ROUTE_ID = "keyRouteId"
        val KEY_SITE_ID = "keySiteId"
        val KEY_USER = "keyUserLogin"

        private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 10 meters

        // The minimum time between updates in milliseconds
        private val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong() // 1 minute

    }

    //private val mFusedLocationClient: FusedLocationProviderClient? = null

    private val user by lazy { intent.getStringExtra(MainActivity.KEY_USER) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_sites)

        val siteListFromIntent  = intent.getParcelableArrayListExtra<RouteSite>(RoutesActivity.KEY_ROUTE_SITES)

        sitesList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            adapter = RoutesAdapter(siteListFromIntent) {


                /*if(distFrom(location.latitude, location.longitude, it.latitude!!.toDouble(), it.longitude!!.toDouble())){
                    toast("I'm close")
                } else { toast("Not")}*/

                startActivity<GatewayActivity>(KEY_USER to user,
                    KEY_ACTIVITY_ID to it.activityId,
                    KEY_ROUTE_ID to it.routeId,
                    KEY_SITE_ID to it.siteId)
            }
        }
    }


    fun distFrom(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Boolean {
        val earthRadius = 6371.01 //  km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)
        val sindLat = Math.sin(dLat / 2)
        val sindLng = Math.sin(dLng / 2)
        val a = Math.pow(sindLat, 2.0) + Math.pow(sindLng, 2.0) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return (earthRadius * c) < 0.2
    }
}
