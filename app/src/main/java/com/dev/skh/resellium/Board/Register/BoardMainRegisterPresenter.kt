package com.dev.skh.resellium.Board.Register

import com.dev.skh.resellium.Base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Seogki on 2018. 10. 11..
 */
class BoardMainRegisterPresenter(val view: View? = null)  : BasePresenter(){


    fun setData(title: String, review: String, grade: String, platform: String) {
        disposable = client.registerBoardData(title, review, grade, platform)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({
                    view?.successData(disposable)
                }, { error ->
                    view?.errorData(disposable, error.message)
                })

    }

    interface View {
        fun successData(disposable: Disposable?)
        fun errorData(disposable: Disposable?, message: String?)

    }
}