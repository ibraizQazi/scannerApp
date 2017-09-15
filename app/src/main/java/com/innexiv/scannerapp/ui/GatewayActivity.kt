package com.innexiv.scannerapp.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import com.innexiv.scannerapp.R
import com.innexiv.scannerapp.adapter.NodesAdapter
import com.innexiv.scannerapp.data.ActivityNodes
import com.innexiv.scannerapp.data.InnexivApi
import com.jakewharton.rxbinding2.view.clickable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_gateway.*
import org.jetbrains.anko.*

class GatewayActivity : AppCompatActivity(), AnkoLogger {

    private var disposable: Disposable? = null

    private val innexivApiService by lazy {
        InnexivApi.create()
    }

    private lateinit var activityNodesObject : ActivityNodes

    companion object {
        private val KEY_ACTIVITY_ID = "keyActivityId"
        private val KEY_NODE_SHORTCODE = "keyNodeShortCode"
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

        scanGateway.setOnClickListener {
            if (!gatewayScanned){
                val i = Intent(this, BarcodeCaptureActivity::class.java)
                i.putExtra(BarcodeCaptureActivity.AutoFocus, true)
                i.putExtra(BarcodeCaptureActivity.UseFlash,true)
                startActivityForResult(i, GATEWAY_BARCODE_CAPTURE )
            } else{

            }
        }
        nodeItems.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@GatewayActivity, LinearLayout.VERTICAL, false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    val barcode: Barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject)
                    activityNodesObject.data.nodeList.forEach {
                        if (it.shortCode == barcode.displayValue){
                            it.isScanned = true
                        }
                    }
                    alert(barcode.displayValue).show()

                } else {
                    info(R.string.barcode_failure)

                }
            }else if (requestCode == CommonStatusCodes.RESOLUTION_REQUIRED) {
                info(String.format(getString(R.string.barcode_error), CommonStatusCodes.getStatusCodeString(resultCode)))
            }
            else {
                info(String.format(getString(R.string.barcode_failure), CommonStatusCodes.getStatusCodeString(resultCode)))
            }
        } else if (requestCode == GATEWAY_BARCODE_CAPTURE){
            gatewayScanned = true
            scanGateway.text = getString(R.string.complete_installation)
            scanGateway.isClickable = false
            getNodes(113)
        } else if (activityComplete){
            scanGateway.isClickable = true
            scanGateway.clearFindViewByIdCache()
            scanGateway.setOnClickListener{
                finish()
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
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
                                    i.putExtra(BarcodeCaptureActivity.AutoFocus, true)
                                    i.putExtra(BarcodeCaptureActivity.UseFlash, false)
                                    startActivityForResult(i, RC_BARCODE_CAPTURE)
                                }
                                //startActivity<BarcodeCaptureActivity>("nodeId" to it.shortCode)
                            }
                        },
                        {
                            error { it.message }
                        }
                )
    }

    private fun areAllNodeRegistered () : Boolean {
        activityNodesObject.data.nodeList.filter { it.isScanned == false}.isEmpty().let {
            activityComplete = true
            return true
        }
    }
}
