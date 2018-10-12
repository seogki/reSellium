package com.dev.skh.resellium.Main.tab.Best

import com.dev.skh.resellium.Base.BasePresenter
import com.dev.skh.resellium.Main.Model.RankModels
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Seogki on 2018. 10. 12..
 */
class HomeMainBestPresenter(val view: View? = null) : BasePresenter(){

    fun getData(){
        disposable = client.getBestMainData()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.getBestData(it, disposable) }
                        , { view?.errorData(disposable, it.message) })
    }

    interface View{
        fun getBestData(best: RankModels?, disposable: Disposable?)
        fun errorData(disposable: Disposable?, message: String?)

    }
}