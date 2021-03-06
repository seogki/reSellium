package com.dev.skh.resellium.Game.Inner

import com.dev.skh.resellium.Base.BasePresenter
import com.dev.skh.resellium.Game.Model.CommentModel
import com.dev.skh.resellium.Game.Model.GameMainModel
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

    fun setReport(item: GameMainModel?) {
        if (item != null) {
            disposable = client.setReport(item.platform!!, item.id!!, item.title!!, item.date!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe({
                        view?.setReport(disposable)
                    }) {
                        view?.setReportError(disposable)

                    }
        }
    }


    interface View {
        fun setError(disposable: Disposable?, message: String?)
        fun setCommentData(result: MutableList<CommentModel>?, disposable: Disposable?, isScroll: Boolean)
        fun onRegisterData(disposable: Disposable?)

        fun setReportError(disposable: Disposable?)
        fun setReport(disposable: Disposable?)

    }
}