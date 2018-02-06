package com.innexiv.scannerapp.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
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
import kotlinx.android.synthetic.main.routes_content.*
import org.jetbrains.anko.*
import java.util.*


class RoutesActivity : AppCompatActivity(), AnkoLogger {

    companion object {
        val KEY_ROUTE_SITES = "routesSiteList"
    }
    private var disposable: Disposable? = null

    private val innexivApiService by lazy {
        InnexivApi.create()
    }
    private lateinit var siteList: List<RouteSite>
    private lateinit var routeResponseObject : RoutesResponse

    private val user by lazy { intent.getStringExtra(MainActivity.KEY_USER) }
    private val password by lazy { intent.getStringExtra(MainActivity.KEY_PASSWORD)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routes)

        //val count = DbManager(database).getCount()
        setSupportActionBar(toolbar)
        initNavigationDrawer()

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

    private fun initNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            when (id) {
                R.id.option_a -> {
                    toast( "You Clicked Device Registration")
                    drawerLayout!!.closeDrawers()
                }
                R.id.option_b -> {
                    toast("You Clicked Field Replacement")
                    drawerLayout!!.closeDrawers()
                }
                R.id.option_c -> {
                    toast("You Clicked On-Site Inventory")
                    drawerLayout!!.closeDrawers()
                }
            }
            true
        }
        val header = navigationView.getHeaderView(0)
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            override fun onDrawerClosed(v: View?) {
                super.onDrawerClosed(v)
            }

            override fun onDrawerOpened(v: View?) {
                super.onDrawerOpened(v)
            }
        }
        drawerLayout!!.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    private fun getRoutes(){
        disposable = innexivApiService.getRoutes(user.toString(), password.toString())
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
                                    i.putParcelableArrayListExtra(KEY_ROUTE_SITES, ArrayList(siteList))
                                    i.putExtra(MainActivity.KEY_USER, user)
                                    i.putExtra(MainActivity.KEY_PASSWORD, password)
                                    startActivity(i)
                                } else
                                     toast ("No sites available for this Route: ${it.name}")

                            }
                            routesList.adapter.notifyDataSetChanged()

                        },
                        {
                            error { it.message }
                            //add a dialog and finish activity on dialog close
                        }
                )

    }

    private fun getRelevantRoutes(id: Int) : List<RouteSite> = routeResponseObject.routeSiteList.filter { it.routeId == id }

}
