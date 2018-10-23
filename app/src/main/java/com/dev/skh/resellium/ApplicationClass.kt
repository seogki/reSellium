package com.dev.skh.resellium

import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

/**
 * Created by Seogki on 2018. 9. 28..
 */
class ApplicationClass : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        MultiDex.install(this)
    }
}