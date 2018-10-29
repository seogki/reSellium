package com.dev.skh.resellium.Game.Inner

import com.dev.skh.resellium.Base.BasePresenter
import com.dev.skh.resellium.Game.Model.GameCommentModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Seogki on 2018. 10. 29..
 */
class GameMainCommentPresenter(val view: View? = null) : BasePresenter() {

    fun getCommentData(gameid: String?, type: String?) {
        if (gameid != null) {
            disposable = client.getCommentData(type!!, gameid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        view?.setCommentData(result, disposable, true)
                    }, { error ->
                        view?.setError(disposable, error.message)
                    })
        }
    }

    fun registerCommentData(type: String, id: String?, data: String) {
        if (id != null) {
            disposable = client.registerCommentData(type, id, data)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe({
                        view?.onRegisterData(disposable)
                    }, { error ->
                        view?.setError(disposable, error.message)
                    })
        }
    }


    interface View {
        fun setError(disposable: Disposable?, message: String?)
        fun setCommentData(result: MutableList<GameCommentModel>?, disposable: Disposable?, isScroll: Boolean)
        fun onRegisterData(disposable: Disposable?)

    }
}