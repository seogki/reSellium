package com.dev.skh.resellium.Main

import com.dev.skh.resellium.Base.BasePresenter
import com.dev.skh.resellium.Main.Model.HoriModel
import com.dev.skh.resellium.Main.Model.PopularModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Seogki on 2018. 10. 1..
 */
class HomeMainPresenter(val view: HomeMainPresenter.View? = null)  : BasePresenter(){


    fun getPopularData(){
        disposable = client.getPopularData()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.getPopularData(it, disposable) }
                        , { view?.errorUpdateData(disposable, it.message) })
    }

    fun getHoriData(currentPos: String?) {
        if (currentPos != null) {
            disposable = client.getMainData(currentPos)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ view?.updateData(it, disposable, currentPos) }
                            , { view?.errorUpdateData(disposable, it.message) })
        }
    }


    interface View {
        fun getPopularData(it: MutableList<PopularModel>?, disposable: Disposable?)

        fun updateData(result: MutableList<HoriModel>?, disposable: Disposable?, currentPos: String)
        fun errorUpdateData(disposable: Disposable?, message: String?)

    }
}