package com.innexiv.scannerapp.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.innexiv.scannerapp.R
import com.innexiv.scannerapp.adapter.RoutesSiteAdapter
import com.innexiv.scannerapp.data.InnexivApi
import com.innexiv.scannerapp.data.RouteSite
import com.innexiv.scannerapp.data.RoutesResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_routes.*
import org.jetbrains.anko.*


class RoutesActivity : AppCompatActivity(), AnkoLogger {

    companion object {
        private val KEY_ROUTE_SITES = "routesSiteList"
    }
    private var disposable: Disposable? = null

    private val innexivApiService by lazy {
        InnexivApi.create()
    }
    private lateinit var siteList: List<RouteSite>
    private lateinit var routeResponseObject : RoutesResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routes)

        //routesList.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        intent?.let {
           toast( it.getStringExtra("token") )
        }
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


    private fun getRoutes(){
        disposable = innexivApiService.getRoutes("shahab.alam@innexiv.com", "test123")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            routeResponseObject = it
                            routesList.adapter = RoutesSiteAdapter(routeResponseObject.routesList){
                                //toast("${it.name} Clicked")
                                if (getRelevantRoutes(it.id).isNotEmpty()) {
                                    siteList = getRelevantRoutes(it.id)
                                    val i = Intent(this, RouteSitesActivity::class.java)
                                    i.putParcelableArrayListExtra(KEY_ROUTE_SITES,ArrayList(siteList))
                                    startActivity(i)
                                } else
                                     toast ("No sites available for this Route: ${it.name}")

                            }
                            routesList.adapter.notifyDataSetChanged()

                        },
                        {
                            error { it.message }
                        }
                )

    }

    private fun getRelevantRoutes(id: Int) : List<RouteSite> = routeResponseObject.routeSiteList.filter { it.routeId == id }

}
