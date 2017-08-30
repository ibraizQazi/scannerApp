package com.innexiv.scannerapp

import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.Snackbar
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.Toast
import com.innexiv.scannerapp.data.InnexivApi
import com.innexiv.scannerapp.data.LoginPostBody
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    private val innexivApiservice by lazy {
        InnexivApi.create()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mngr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        login.setOnClickListener{
            if (email.text.isNotEmpty() && email_password.text.isNotEmpty()) {

                val loginDetails = LoginPostBody("ibraiz.qazi@innexiv.com", "ibraiz123","123123123")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    toast(mngr.imei.toString())
                }

                loginUser(loginDetails)
            }
        }

    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    fun loginUser (loginDetails: LoginPostBody){
        disposable = innexivApiservice.loginUser(loginDetails)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            result -> toast(result.toString())
                            alert("Show sites").show()
                        },
                        {
                            error -> toast(error.toString())
                        }
                )

    }
}
