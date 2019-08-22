package com.mvp.accontrol

import android.app.Application
import com.mvp.accontrol.helper.RetrofitRestClient

class ACControllerApplication : Application() {
    companion object {
        lateinit var instance: ACControllerApplication
            private set
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        initRemoteClient()
    }

    fun changeBaseUrl(baseUrl: String) {
        RetrofitRestClient.configureBaseUrl(baseUrl)
    }

    private fun initRemoteClient() {
        //this url is just a demo, SSDP will change it later on
        val baseUrl = "http://192.168.0.104:1900/"

        RetrofitRestClient
            .configureConverterFactory()
            .configureBaseUrl(baseUrl)
            .configureOkHttp()
    }
}
