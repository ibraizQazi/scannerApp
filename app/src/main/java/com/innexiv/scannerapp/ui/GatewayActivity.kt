package com.innexiv.scannerapp.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import com.innexiv.scannerapp.R
import com.innexiv.scannerapp.adapter.NodesAdapter
import com.innexiv.scannerapp.data.ActivityNodes
import com.innexiv.scannerapp.data.DbNodeModel
import com.innexiv.scannerapp.data.InnexivApi
import com.innexiv.scannerapp.db.DbManager
import com.innexiv.scannerapp.db.NodeTable
import com.innexiv.scannerapp.db.database
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_gateway.*
import org.jetbrains.anko.*
import org.jetbrains.anko.db.insert

class GatewayActivity : AppCompatActivity(), AnkoLogger {

    private var disposable: Disposable? = null

    private val innexivApiService by lazy {
        InnexivApi.create()
    }

    private lateinit var activityNodesObject : ActivityNodes

    companion object {
        private val KEY_ACTIVITY_ID = "keyActivityId"
        private val KEY_ROUTE_ID = "keyRouteId"
        private val KEY_SITE_ID = "keySiteId"
        private val KEY_USER_ID = "keyUserId"
        private val RC_BARCODE_CAPTURE = 9001
        private val GATEWAY_BARCODE_CAPTURE = 8001

    }
    private var gatewayScanned : Boolean = false
    private var activityComplete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gateway)
        val activityId = intent.extras[KEY_ACTIVITY_ID]
        //toast ("Site id = $activityId")

        gatewayInfo.text = activityId.toString()
        gatewayInfo2.text = activityComplete.toString()

        getNodes(113)       //activityId from previous activity

        nodeItems.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@GatewayActivity, LinearLayout.VERTICAL, false)
            visibility = View.GONE
        }

        scanGateway.setOnClickListener {
            if (!gatewayScanned){
                val i = Intent(this, BarcodeCaptureActivity::class.java)
                i.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked)
                i.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked)
                i.putExtra(BarcodeCaptureActivity.AutoCapture, autoCapture.isChecked)

                startActivityForResult(i, GATEWAY_BARCODE_CAPTURE )
            } else if(areAllNodeRegistered()) {
                toast("activity should be complete.")
                scanGateway.visibility = View.VISIBLE
                nodeItems.visibility = View.VISIBLE
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when(requestCode){
            RC_BARCODE_CAPTURE ->
                when (resultCode) {
                    CommonStatusCodes.SUCCESS -> data?.let {
                        val barcode: Barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject)
                        activityNodesObject.data.nodeList.forEach {

                            if(barcode.displayValue.contains(it.shortCode)) {
                                it.isScanned = true
                                toast("${it.shortCode} Found in ${barcode.displayValue}")
                                DbManager(database).saveScannedNodes(DbNodeModel(911,
                                        4,
                                        113,
                                        it.equipmentId,
                                        it.shortCode,
                                        "20/09/2017",
                                        "Ibraiz",
                                        "123123123"))
                            } else toast("Not found!")
                    }
                }
                    CommonStatusCodes.ERROR -> info(R.string.barcode_error)

                    else -> info(String.format(getString(R.string.barcode_failure), CommonStatusCodes.getStatusCodeString(resultCode)))
                }

            CommonStatusCodes.RESOLUTION_REQUIRED ->
                info(String.format(getString(R.string.barcode_error), CommonStatusCodes.getStatusCodeString(resultCode)))

            GATEWAY_BARCODE_CAPTURE -> {
                gatewayScanned = true
                scanGateway.text = getString(R.string.complete_installation)
                gatewayCard.visibility = View.GONE
                nodeItems.visibility = View.VISIBLE
            }

            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
    private fun getNodes (activityId: Int){
        disposable = innexivApiService.getNodesInfo(activityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            activityNodesObject = it
                            nodeItems.adapter = NodesAdapter(activityNodesObject.data.nodeList){
                                //toast("Equipment : ${it.shortCode}")
                                if (it.isScanned.not()) {
                                    val i = Intent(this, BarcodeCaptureActivity::class.java)
                                    i.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked)
                                    i.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked)
                                    i.putExtra(BarcodeCaptureActivity.AutoCapture, autoCapture.isChecked)
                                    startActivityForResult(i, RC_BARCODE_CAPTURE)
                                }
                            }
                        },
                        {
                            error { it.message }
                        }
                )
    }

    private fun areAllNodeRegistered () : Boolean {
        activityNodesObject.data.nodeList.none { !it.isScanned }.let {
            activityComplete = true
            return activityComplete
        }
    }
}
