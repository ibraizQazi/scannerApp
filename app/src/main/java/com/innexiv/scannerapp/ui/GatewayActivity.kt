package com.innexiv.scannerapp.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.telephony.TelephonyManager
import android.view.View
import android.widget.LinearLayout
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import com.innexiv.scannerapp.R
import com.innexiv.scannerapp.adapter.NodesAdapter
import com.innexiv.scannerapp.commons.ViewType
import com.innexiv.scannerapp.data.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_gateway.*
import org.jetbrains.anko.*
import java.util.*
import kotlin.collections.LinkedHashMap

@SuppressLint("MissingPermission")
class GatewayActivity : AppCompatActivity(), AnkoLogger, View.OnClickListener {

    private var disposable: Disposable? = null

    private val innexivApiService by lazy {
        InnexivApi.create()
    }

    companion object {
        private val RC_BARCODE_CAPTURE = 9001
        private val GATEWAY_BARCODE_CAPTURE = 8001
        val GATEWAY_SHORTCODE = "keyGatewayShortcode"
        private val DATA_ITEM_LIST = "keyDataItemList"
    }

    private var activityComplete = false

    private lateinit var activityNodes : ActivityNodes
    private lateinit var list: List<DataItem>
    private lateinit var equipmentTypeList: List<EquipmentLayer>

    private val user by lazy { intent.extras[RouteSitesActivity.KEY_USER] }
    private val routeId by lazy { intent.extras[RouteSitesActivity.KEY_ROUTE_ID] }
    private val siteId by lazy { intent.extras[RouteSitesActivity.KEY_SITE_ID] }
    private val activityId by lazy { intent.extras[RouteSitesActivity.KEY_ACTIVITY_ID] }

    private val imei by lazy {
        val mngr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mngr.imei ?: "91919191"
        } else {
            mngr.deviceId ?: "123123123"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gateway)

        // getNodes(activityId as Int)       //activityId from previous activity
        getNodes(113)
        //longToast ("Activity id = $activityId \n $user \n $routeId \n $siteId \n $imei")
        activityInfo.text = "Activity ID: 113"

        nodeItems.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(this@GatewayActivity, LinearLayout.VERTICAL, false)
        }

        finishActivity.isClickable = activityComplete
        finishActivity.setOnClickListener(this)

        savedInstanceState?.let {

            if(it.containsKey(DATA_ITEM_LIST)){
                activityNodes = it.getParcelable(DATA_ITEM_LIST)
            } else {
                getNodes(113)
            }
        }
    }

    override fun onClick(gatewayBtn: View?) {
        if(areAllNodeRegistered()) {

            info("Activity completed.")
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode != Activity.RESULT_CANCELED && data != null){

            val barcode: Barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject)

            when (requestCode) {
                RC_BARCODE_CAPTURE ->
                    when (resultCode) {

                        CommonStatusCodes.SUCCESS -> barcode.let {
                            debug("barcode RC : ${it.displayValue}")
                            checkBarcode(it)
                        }

                        CommonStatusCodes.ERROR -> info(R.string.barcode_error)

                        else -> {
                            info("should not be here")
                            info(String.format(getString(R.string.barcode_failure), CommonStatusCodes.getStatusCodeString(resultCode)))
                        }
                    }

                CommonStatusCodes.RESOLUTION_REQUIRED ->
                    info(String.format(getString(R.string.barcode_error), CommonStatusCodes.getStatusCodeString(resultCode)))

            /*GATEWAY_BARCODE_CAPTURE -> {
                        when(resultCode){
                            CommonStatusCodes.SUCCESS -> barcode.let {
                                info("barcode : ${it.displayValue}")

                            }

                            else -> error("barcode not captured")
                        }

                    }
    */
                else -> super.onActivityResult(requestCode, resultCode, data)
            }
        } else super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        val dataAndEquipment = ActivityNodes(equipmentTypeList, list)
        activityNodes.let {
            outState?.putParcelable(DATA_ITEM_LIST, activityNodes.copy(equipmentLayer = dataAndEquipment.equipmentLayer, data = dataAndEquipment.data))
        }
    }

    private fun getNodes (activityId: Int){
        disposable = innexivApiService.getNodesInfo(activityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            list = it.data
                            equipmentTypeList = it.equipmentLayer

                            activityNodes = ActivityNodes(equipmentTypeList,list)

                            initAdapter(getFinalList(toMap(equipmentTypeList)))

                        },
                        {

                            error { it.message }
                        }
                )
    }

    private fun getFinalList(map: LinkedHashMap<EquipmentLayer, List<DataItem>>) : List<ViewType> {

        val finalViewList = mutableListOf<ViewType>()

        for (key in map.keys) {

            val equipment = EquipmentItem(key)

            if (map[key]?.isNotEmpty()!!) {

                finalViewList.add(equipment)

                map[key]?.forEach { item ->

                    info("data : ${item.name}")
                    finalViewList.add(DataListItem(item))
                }
            }
        }

        return finalViewList
    }

    private fun toMap(dataList: List<EquipmentLayer>) : LinkedHashMap<EquipmentLayer, List<DataItem>> {
        val map = linkedMapOf<EquipmentLayer, List<DataItem>>()
        for (it in dataList) {
            info("${it.name} size: ${getFilteredList(it.id).size}")
            val filteredList = getFilteredList(it.id)
            if (filteredList.isNotEmpty())
                map.put(it, filteredList)
        }
        return map
    }

    private fun getFilteredList(id: Int) : List<DataItem> =
            list.filter { it.equipmentLayerId == id }

    private fun getSortedList(itemList: MutableList<DataItem>) : List<DataItem> =
         itemList
                 .filter { it.equipmentLayerName != "Gateway" && it.equipmentId != 2 }
                 .sortedBy { it.equipmentLayerName }

    private fun getGateway (itemList: List<DataItem>) : DataItem =
         itemList.asSequence()
                 .first { it.equipmentLayerId == 2 && it.equipmentLayerName == "Gateway"}


    private fun checkBarcode(barcode: Barcode) : Boolean{
        var isFound = false
        list.forEach {

            if (barcode.displayValue.contains(it.shortCode)){

                it.isScanned = true
                debug("${it.name} Found")

                /*DbManager(database).saveScannedNodes(DbNodeModel(
                                    routeID = 911,
                                    siteId = 4,
                                    activityId = 113,
                                    equipmentId =  it.equipmentId,
                                    barcode =  it.shortCode,
                                    scannedOn =  "26/09/2017",
                                    scannedBy =  user.toString(),
                                    deviceImei =  imei))*/
                isFound = true

            } else {
                isFound = false
            }

        }
        nodeItems.adapter.notifyDataSetChanged()
        return isFound
    }

    private fun initAdapter(finalViewList: List<ViewType> )  {

        nodeItems.adapter = NodesAdapter(finalViewList){

            if (it.isScanned.not()) {
                val i = Intent(this, BarcodeCaptureActivity::class.java)
                i.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked)
                i.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked)
                i.putExtra(BarcodeCaptureActivity.AutoCapture, autoCapture.isChecked)
                startActivityForResult(i, RC_BARCODE_CAPTURE)
            }

        }
    }

    private fun areAllNodeRegistered () : Boolean = list.none { !it.isScanned }

}
