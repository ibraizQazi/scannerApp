package com.innexiv.scannerapp.ui

import android.annotation.SuppressLint
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
import com.innexiv.scannerapp.extensions.isNetworkConnected
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

    companion object {
        private val LOGIN_SUCCESS = "success"
        private val KEY_USER = "keyUserId"
        private val KEY_TOKEN = "keyToken"
    }
    private var disposable: Disposable? = null

    private val innexivApiService by lazy {
        InnexivApi.create()
    }


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mngr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        login.setOnClickListener{
            if (email.text.isNotEmpty() && email_password.text.isNotEmpty()){

               /* val loginDetails = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LoginPostBody(email.text.toString(),email_password.text.toString(), imei = mngr.imei)
                } else {
                    LoginPostBody(email.text.toString(),email_password.text.toString(), imei = mngr.deviceId)
                }*/

                val loginDetails = LoginPostBody("ibraiz.qazi@innexiv.com", "ibraiz123","123123123")

                isNetworkConnected().let {
                        loginUser(loginDetails)
                    }
            } else{
                Snackbar.make(topLayout, "Enter Email or Password", Snackbar.LENGTH_LONG).show()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    private fun loginUser (loginDetails: LoginPostBody){
        disposable = innexivApiService.simpleFormLoginUser(loginDetails.email, loginDetails.password, loginDetails.imei)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            //result?.token?.let { toast(it) }
                            if (it.status == LOGIN_SUCCESS && it.token.isNotEmpty())
                                startActivity<RoutesActivity>(KEY_TOKEN to it.token,
                                        KEY_USER to loginDetails.email)

                        },
                        {
                            error -> debug(error)
                            alert(error.localizedMessage).show()
                        }
                )

    }
}
