package com.dev.skh.resellium.Base

import com.dev.skh.resellium.Network.ApiCilentRx
import io.reactivex.disposables.Disposable

/**
 * Created by Seogki on 2018. 10. 12..
 */
open class BasePresenter{
    var disposable: Disposable? = null
    val client by lazy { ApiCilentRx.create() }



}