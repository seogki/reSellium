package com.dev.skh.resellium.Main.tab.Worst

import com.dev.skh.resellium.Base.BasePresenter
import com.dev.skh.resellium.Board.Model.BoardMainModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Seogki on 2018. 10. 12..
 */
class HomeMainWorstPresenter(val view: View? = null) : BasePresenter(){


    fun getData(){
        disposable = client.getWorstMainData()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.getNewData(it, disposable) }
                        , { view?.errorData(disposable, it.message) })
    }

    interface View{
        fun getNewData(arr: MutableList<BoardMainModel>?, disposable: Disposable?)
        fun errorData(disposable: Disposable?, message: String?)

    }
}