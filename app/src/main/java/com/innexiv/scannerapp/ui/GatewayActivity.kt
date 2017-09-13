package com.innexiv.scannerapp.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.innexiv.scannerapp.R
import com.innexiv.scannerapp.adapter.RoutesAdapter
import com.innexiv.scannerapp.data.RouteSite
import kotlinx.android.synthetic.main.activity_gateway.*
import org.jetbrains.anko.*


class GatewayActivity : AppCompatActivity(), AnkoLogger {

    /*private var disposable: Disposable? = null

    private val innexivApiService by lazy {
        InnexivApi.create()
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gateway)

        val siteListFromIntent  = intent.getParcelableArrayListExtra<RouteSite>("routesSiteList")

        sitesList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            adapter = RoutesAdapter(siteListFromIntent){
                val pos = distFrom(33.69, 73.05, it.latitude!!.toDouble(), it.longitude!!.toDouble())
                toast("${it.id} in radius? Ans: $pos")
                //val i = Intent(this@GatewayActivity, BarcodeCaptureActivity::class.java)
            }
        }
        //getSites()
    }

    override fun onPause() {
        super.onPause()
        //disposable?.dispose()
    }

 /*   fun getSites(){
        disposable = innexivApiService.getSiteData("shahab.alam@innexiv.com","test123")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        toast(it.siteList.size.toString())
                        sitesList.adapter = SitesAdapter(it.siteList){

                            startActivity<BarcodeCaptureActivity>()
                        }
                        sitesList.adapter.notifyDataSetChanged()

                    },
                    {
                        alert(it.message?:"error: ${it.message}").show()
                    }
                )
    }*/


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
