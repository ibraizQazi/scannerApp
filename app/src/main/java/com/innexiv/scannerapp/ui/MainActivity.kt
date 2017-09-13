package com.innexiv.scannerapp.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.Snackbar
import android.os.Bundle
import android.telephony.TelephonyManager
import com.innexiv.scannerapp.R
import com.innexiv.scannerapp.data.InnexivApi
import com.innexiv.scannerapp.data.LoginPostBody
import com.innexiv.scannerapp.data.LoginResponse
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import org.jetbrains.anko.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.RequestBody




class MainActivity : AppCompatActivity(), AnkoLogger {

    private var disposable: Disposable? = null

    private val innexivApiService by lazy {
        InnexivApi.create()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val mngr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        login.setOnClickListener{
            //if (email.text.isNotEmpty() && email_password.text.isNotEmpty()) {

                val loginDetails = LoginPostBody("ibraiz.qazi@innexiv.com", "ibraiz123","123123123")


                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    toast(mngr.imei.toString())
                }*/

            isNetworkConnected().let {
                    loginUser(loginDetails)
                }
            //}
        }

    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    fun loginUser (loginDetails: LoginPostBody){
        disposable = innexivApiService.simpleFormLoginUser(loginDetails.email, loginDetails.password, loginDetails.imei)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            result ->
                            //result?.token?.let { toast(it) }
                            startActivity<RoutesActivity>("token" to result.token)
                        },
                        {
                            error -> debug(error)
                        }
                )

    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
