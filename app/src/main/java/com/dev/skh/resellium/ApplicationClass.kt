package com.dev.skh.resellium

import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication

/**
 * Created by Seogki on 2018. 9. 28..
 */
class ApplicationClass : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }
}