package com.dev.skh.resellium.Base

import com.dev.skh.resellium.Dagger.ApiClientTest
import com.dev.skh.resellium.Dagger.DaggerMagicBox
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created by Seogki on 2018. 10. 12..
 */
open class BaseRepository {
//    var disposable: MutableLiveData<Disposable>? = null
    var disposable: Disposable? = null
    //    val client by lazy { ApiCilentRx.create() }
    val client by lazy { retrofit.create() }


    @Inject
    lateinit var retrofit: ApiClientTest

    init {
        DaggerMagicBox.create().viewModel(this)
    }


}