package com.dev.skh.resellium.Main.tab.New

import com.dev.skh.resellium.Base.BasePresenter
import com.dev.skh.resellium.Main.Model.RankModels
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Seogki on 2018. 10. 12..
 */
class HomeMainNewPresenter(val view: View? = null) : BasePresenter(){


    fun getData(){
        disposable = client.getNewMainData()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.getNewData(it, disposable) }
                        , { view?.errorData(disposable, it.message) })
    }

    interface View{
        fun getNewData(new: RankModels?, disposable: Disposable?)
        fun errorData(disposable: Disposable?, message: String?)

    }
}