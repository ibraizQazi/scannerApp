package com.innexiv.scannerapp.data

import com.innexiv.scannerapp.BuildConfig
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Retrofit
import io.reactivex.Observable
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


interface InnexivApi {

    @FormUrlEncoded
    @POST("index.php?r=sitedna/iscanlogin")
    fun simpleFormLoginUser(@Field("email", encoded = true) emailAddress: String,
                            @Field("password", encoded = true) password: String,
                            @Field("imei", encoded = true) imei: String) : Observable<LoginResponse>

    @POST("index.php?r=sitedna/barcodescanner/")
    fun sendBarcode(@Body barcodePost: BarcodePost) : Call<BarcodeResponse>

    @FormUrlEncoded
    @POST("index.php?r=site/getroute")
    fun getRoutes(@Field("id", encoded = true) id: String,
                  @Field("pass", encoded = true) password: String) : Observable<RoutesResponse>

    @FormUrlEncoded
    @POST("index.php?r=sitedna/getequipmentlayer")
    fun getNodesInfo(@Field("activity_id", encoded = true) activityId: Int) : Observable<ActivityNodes>

    companion object {
        fun create(): InnexivApi {

            val client = OkHttpClient().newBuilder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                    })
                    .build()

            val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl("http://115.186.155.20:5051/innexiverp/")
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build()

            return retrofit.create(InnexivApi::class.java)
        }
    }
}
