package com.dev.skh.resellium.Board.Sub

import com.dev.skh.resellium.Base.BasePresenter
import com.dev.skh.resellium.Board.Model.BoardMainModel
import com.dev.skh.resellium.Game.Model.CommentModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Seogki on 2018. 10. 30..
 */
class InnerBoardMainPresenter(val view: View? = null) : BasePresenter() {

    fun getCommentData(gameid: String?) {
        if (gameid != null) {
            disposable = client.getCommentData("3", gameid)
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

    fun registerCommentData(id: String?, data: String) {
        if (id != null) {
            disposable = client.registerCommentData("3", id, data)
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

    fun setReport(item: BoardMainModel?) {
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