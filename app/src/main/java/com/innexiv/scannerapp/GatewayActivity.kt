package com.innexiv.scannerapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.innexiv.scannerapp.adapter.SitesAdapter
import com.innexiv.scannerapp.data.InnexivApi
import com.innexiv.scannerapp.data.SiteResponse
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_gateway.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import org.jetbrains.anko.error
import org.jetbrains.anko.toast


class GatewayActivity : AppCompatActivity(), AnkoLogger {

    private var disposable: Disposable? = null

    private val innexivApiService by lazy {
        InnexivApi.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gateway)

        //sitesList.layoutManager = LinearLayoutManager(this,LinearLayout.VERTICAL, false)

        sitesList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        }
        getSites()
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    fun getSites(){
        disposable = innexivApiService.getSiteData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        sitesList.adapter = SitesAdapter(it.siteList)
                        sitesList.adapter.notifyDataSetChanged()
                    },
                    {
                        alert(it.message?:"error: ${it.message}").show()
                    }
                )
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
