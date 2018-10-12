package com.dev.skh.resellium.Game.Register

import com.dev.skh.resellium.Base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Seogki on 2018. 10. 1..
 */
class GameRegisterPresenter(val view: View? = null) : BasePresenter(){

    fun sendData(platform: String, title: String, place: String, money: String, which: String) {
        disposable = client.RegisterData(platform, title, place, money, which)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({
                    view?.registerData(disposable)
                }, { error ->
                    view?.errorData(disposable, error.message)
                })
    }

    interface View{
        fun registerData(disposable: Disposable?)
        fun errorData(disposable: Disposable?, error: String?)
    }
}