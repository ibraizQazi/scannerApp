package com.innexiv.scannerapp

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.Snackbar
import android.os.Bundle
import android.telephony.TelephonyManager
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

    private val innexivApiservice by lazy {
        InnexivApi.create()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mngr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        login.setOnClickListener{
            //if (email.text.isNotEmpty() && email_password.text.isNotEmpty()) {

                val loginDetails = LoginPostBody("ibraiz.qazi@innexiv.com", "ibraiz123","123123123")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    toast(mngr.imei.toString())
                }

                loginUser(loginDetails)
            //}
        }

    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    fun loginUser (loginDetails: LoginPostBody){
        disposable = innexivApiservice.simpleFormLoginUser(loginDetails.email, loginDetails.password, "123123123")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            result -> alert(result?.status +"Show sites").show()
                            startActivity<GatewayActivity>()
                        },
                        {
                            error -> toast(error.toString())
                        }
                )

    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager// 1
        val networkInfo = connectivityManager.activeNetworkInfo // 2
        return networkInfo != null && networkInfo.isConnected // 3
    }
}
