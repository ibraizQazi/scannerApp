package com.innexiv.scannerapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.innexiv.scannerapp.adapter.RoutesAdapter
import com.innexiv.scannerapp.data.InnexivApi
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_routes.*
import okhttp3.Response
import okhttp3.ResponseBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity


class RoutesActivity : AppCompatActivity(), AnkoLogger {

    private var disposable: Disposable? = null

    private val innexivApiService by lazy {
        InnexivApi.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routes)

        //routesList.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        routesList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@RoutesActivity, LinearLayout.VERTICAL, false)
        }

        getRoutes()
    }
    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    fun getRoutes(){
        disposable = innexivApiService.getRoutes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            t: ResponseBody? ->  debug(t.toString())
                            //routesList.adapter = RoutesAdapter()
                            startActivity<GatewayActivity>()

                        },
                        {
                            error { it.message }
                        }
                )

    }
}
