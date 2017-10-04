package com.innexiv.scannerapp.ui

import android.annotation.SuppressLint
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
import com.innexiv.scannerapp.adapter.NodeDataAdapter
import com.innexiv.scannerapp.data.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_gateway.*
import org.jetbrains.anko.*
import org.w3c.dom.Node
import java.util.*

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
    private var gatewayScanned = false
    private var activityComplete = false

    private lateinit var activityNodes : ActivityNodes
    private lateinit var list: List<dataItem>
    private lateinit var equipmentTypeList: List<EquipmentLayer>
    private lateinit var gatewayNode: dataItem

    private lateinit var nodeAdapter : NodeDataAdapter

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

        // /getNodes(activityId as Int)       //activityId from previous activity
        getNodes(113)
        //longToast ("Activity id = $activityId \n $user \n $routeId \n $siteId \n $imei")

        nodeItems.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@GatewayActivity, LinearLayout.VERTICAL, false)
        }

        scanGateway.setOnClickListener(this)

        savedInstanceState?.let {

            if(it.containsKey(DATA_ITEM_LIST)){
                activityNodes = it.getParcelable(DATA_ITEM_LIST)
            } else {
                getNodes(113)
            }
        }
    }

    override fun onClick(gatewayBtn: View?) {
        if (!gatewayScanned){

            val i = Intent(this, BarcodeCaptureActivity::class.java)
            i.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked)
            i.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked)
            i.putExtra(BarcodeCaptureActivity.AutoCapture, autoCapture.isChecked)
            startActivityForResult(i, GATEWAY_BARCODE_CAPTURE )

        } else if(areAllNodeRegistered()) {

            toast("activity should be complete.")
            //scanGateway.visibility = View.VISIBLE
            //nodeItems.visibility = View.VISIBLE
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        val barcode: Barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject)

            when (requestCode) {
                RC_BARCODE_CAPTURE ->
                    when (resultCode) {

                        CommonStatusCodes.SUCCESS -> barcode.let {
                            toast("barcode RC : ${it.displayValue}")
                            checkBarcode(it)
                        }

                        CommonStatusCodes.ERROR -> info(R.string.barcode_error)

                        else -> {
                            toast("should not be here")
                            info(String.format(getString(R.string.barcode_failure), CommonStatusCodes.getStatusCodeString(resultCode)))
                        }
                    }

                CommonStatusCodes.RESOLUTION_REQUIRED ->
                    info(String.format(getString(R.string.barcode_error), CommonStatusCodes.getStatusCodeString(resultCode)))

                GATEWAY_BARCODE_CAPTURE -> {
                    when(resultCode){
                        CommonStatusCodes.SUCCESS -> barcode.let {
                            toast("barcode : ${it.displayValue}")
                            gatewayScanned = checkGatewayBarcode(it)
                        }

                        else -> toast("barcode not captured")
                    }

                }

                else -> super.onActivityResult(requestCode, resultCode, data)
            }

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
                            list = getSortedList(it.data.toMutableList())
                            list.groupBy { it.equipmentLayerId }

                            equipmentTypeList = it.equipmentLayer

                            activityNodes = ActivityNodes(equipmentTypeList,list)

                            makeCompleteList(activityNodes)?.let { initAdapter(it) }

                            gatewayNode = getGateway(it.data)

                            gatewayInfo.text = gatewayNode.name

                        },
                        {
                            error { it.message }
                        }
                )
    }
    private fun makeCompleteList(activityNodes: ActivityNodes): List<EquipmentType>? {
        var finalList : MutableList<EquipmentType>? = null
        for (equipmentLayer in activityNodes.equipmentLayer) {
            finalList?.add(EquipmentType(equipmentLayer.name,makeDataList(equipmentLayer, activityNodes.data)))
            toast("${finalList?.first()?.title}")
        }
        return finalList
    }


    private fun makeDataList (equipmentObj : EquipmentLayer, itemList: List<dataItem>) : List<dataItem> =
        itemList.filter { it.equipmentId == equipmentObj.id && it.equipmentLayerName == equipmentObj.name }


    private fun getSortedList(itemList: MutableList<dataItem>) : List<dataItem> =
         itemList.filter { it.equipmentLayerName != "Gateway" && it.equipmentId != 2 }
                 //.sortedBy { it.equipmentLayerName }


    private fun getGateway (itemList: List<dataItem>) : dataItem =
         itemList.asSequence()
                 .first { it.equipmentLayerId == 2 && it.equipmentLayerName == "Gateway"}


    private fun checkBarcode(barcode: Barcode) : Boolean{
        var isFound = false
        list.forEach {

            if (barcode.displayValue.contains(it.shortCode)){

                it.isScanned = true
                toast("${it.name} Found")

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
        return isFound
    }

    private fun checkGatewayBarcode (barcode: Barcode) : Boolean{


        var isGateway : Boolean
        //trimming last 5 characters               gatewayNode.shortCode
        //if (barcodeString.dropLast(5).contentEquals("")) {
        //var barcodeString = barcode.displayValue.toString()
        var barcodeString = "RAN-RTU-PMCL-R1A"
        if (barcodeString == gatewayNode.shortCode){

            gatewayNode.isScanned = true
            scanGateway.text = getString(R.string.complete_installation)
            //gatewayCard.visibility = View.GONE
            nodeItems.visibility = View.VISIBLE

            isGateway = true
        } else {
            toast("Not the right gateway device ${gatewayNode.shortCode.dropLast(5)}")
            isGateway = false
        }
        return isGateway
    }

    private fun initAdapter(itemList : List<EquipmentType>)  {
        /*nodeItems.adapter = NodesAdapter(itemList) {

            if (it.isScanned.not()) {
                val i = Intent(this, BarcodeCaptureActivity::class.java)
                i.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked)
                i.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked)
                i.putExtra(BarcodeCaptureActivity.AutoCapture, autoCapture.isChecked)
                startActivityForResult(i, RC_BARCODE_CAPTURE)
            }

        }*/
        nodeAdapter = NodeDataAdapter(itemList)
        nodeItems.adapter = nodeAdapter


    }

    private fun areAllNodeRegistered () : Boolean = list.none { !it.isScanned }

}
