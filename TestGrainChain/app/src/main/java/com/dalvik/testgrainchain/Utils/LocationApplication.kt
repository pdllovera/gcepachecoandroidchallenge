package com.dalvik.testgrainchain.Utils

import android.app.Application

class LocationApplication: Application()  {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object{
        lateinit var instance: LocationApplication
    }
}