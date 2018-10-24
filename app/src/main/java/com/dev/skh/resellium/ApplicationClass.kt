package com.dev.skh.resellium

import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import io.fabric.sdk.android.Fabric

/**
 * Created by Seogki on 2018. 9. 28..
 */
class ApplicationClass : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        MultiDex.install(this)
        MobileAds.initialize(this, "ca-app-pub-2973552036061869~1827299488")
        AdRequest.Builder().addTestDevice("A86E700BF47A5B43A7D1B1882060F2AA")
        AdRequest.Builder().addTestDevice("945C9CAA6FF2EC9D7AE09BE4244D1081")

    }
}