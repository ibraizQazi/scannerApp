package com.innexiv.scannerapp.data

import com.innexiv.scannerapp.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface InnexivApi {

    @POST("index.php?r=sitedna/iscanlogin")
    fun loginUser(@Body loginPostBody: LoginPostBody) : Observable<LoginResponse>

    @POST("index.php?r=sitedna/barcodescanner/")
    fun sendBarcode(@Body barcodePost: BarcodePost) : Observable<BarcodeResponse>

    @GET("index.php?r=sitedna/")
    fun getSiteData()

    companion object {
        fun create(): InnexivApi {

            val client = OkHttpClient().newBuilder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                    })
                    .build()

            val retrofit = Retrofit.Builder()
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://115.186.155.20:5051/innexiverp/")
                    .build()

/*            val retrofit = Retrofit.Builder()
                    .addConverterFactory(MoshiConverterFactory.create())
                    .baseUrl("http://115.186.155.20:5051/innexiverp/")
                    .build()*/

            return retrofit.create(InnexivApi::class.java)
        }
    }
}
