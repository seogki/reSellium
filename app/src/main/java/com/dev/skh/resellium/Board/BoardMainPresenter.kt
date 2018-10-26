package com.dev.skh.resellium.Board

import com.dev.skh.resellium.Base.BasePresenter
import com.dev.skh.resellium.Board.Model.BoardMainModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Seogki on 2018. 10. 11..
 */
class BoardMainPresenter(val view: View? = null)  : BasePresenter(){


    fun getBoardData() {
        disposable = client.getBoardData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({
                    view?.registerData(it, disposable, false)
                }, { error ->
                    view?.errorData(disposable, error.message)
                })
    }

    fun getScrollData(id: String?) {
        if (id != null) {
            disposable = client.getScrollBoardData(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe({
                        view?.registerData(it, disposable, true)
                    }, { error ->
                        view?.errorData(disposable, error.message)
                    })
        }
    }

    fun getSpinnerData(spinner: String?) {
        if(spinner != null) {
            disposable = client.getSpinnerBoardData(spinner)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe({
                        view?.registerSpinnerData(it, disposable, false)
                    }, { error ->
                        view?.errorData(disposable, error.message)
                    })
        }
    }

    fun getSpinnerScrollData(spinner: String?, id: String?) {
        if (spinner != null && id != null) {
            disposable = client.getSpinnerScrollBoardData(spinner, id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe({
                        view?.registerSpinnerData(it, disposable, true)
                    }, { error ->
                        view?.errorData(disposable, error.message)
                    })
        }
    }

    interface View {
        fun registerData(board: MutableList<BoardMainModel>?, disposable: Disposable?, isScroll: Boolean)
        fun errorData(disposable: Disposable?, message: String?)
        fun registerSpinnerData(board: MutableList<BoardMainModel>?, disposable: Disposable?, isScroll: Boolean)
    }
}