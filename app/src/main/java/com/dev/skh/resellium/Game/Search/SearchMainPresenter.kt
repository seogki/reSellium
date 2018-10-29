package com.dev.skh.resellium.Game.Search

import com.dev.skh.resellium.Base.BasePresenter
import com.dev.skh.resellium.Game.Model.GameMainModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Seogki on 2018. 10. 4..
 */
class SearchMainPresenter(val view: View? = null) : BasePresenter() {

    fun getData(currentPos: String?, search: String) {
        when (currentPos) {
            "PS" -> setPs4Data(search)
            "XBOX" -> setXboxData(search)
            "SWITCH" -> setSwitchData(search)
        }
    }

    private fun setSwitchData(search: String) {
        disposable = client.getSearchSwitchData(search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({ result ->
                    view?.setSwitchData(result, disposable, false)
                }, { error ->
                    view?.setError(disposable, error.message)
                })
    }

    private fun setXboxData(search: String) {
        disposable = client.getSearchXboxData(search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({ result ->
                    view?.setXboxData(result, disposable, false)
                }, { error ->
                    view?.setError(disposable, error.message)
                })
    }

    private fun setPs4Data(search: String) {
        disposable = client.getSearchPs4Data(search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({ result ->
                    view?.setPs4Data(result, disposable, false)
                }, { error ->
                    view?.setError(disposable, error.message)
                })
    }


    fun getScrollData(id: String?, currentPos: String?, searchString: String) {
        if (id != null) {
            when (currentPos) {
                "PS" -> setScrollPs4Data(id, searchString)
                "XBOX" -> setScrollXboxData(id, searchString)
                "SWITCH" -> setScrollSwitchData(id, searchString)
            }
        }
    }


    private fun setScrollSwitchData(id: String, searchString: String) {
        disposable = client.getSearchScrollSwitchData(id, searchString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({ result ->
                    view?.setSwitchData(result, disposable, true)
                }, { error ->
                    view?.setError(disposable, error.message)
                })
    }

    private fun setScrollXboxData(id: String, searchString: String) {
        disposable = client.getSearchScrollXboxData(id, searchString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({ result ->
                    view?.setXboxData(result, disposable, true)
                }, { error ->
                    view?.setError(disposable, error.message)
                })
    }

    private fun setScrollPs4Data(id: String, searchString: String) {
        disposable = client.getSearchScrollPs4Data(id, searchString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({ result ->
                    view?.setPs4Data(result, disposable, true)
                }, { error ->
                    view?.setError(disposable, error.message)
                })
    }

    interface View {
        fun setPs4Data(result: MutableList<GameMainModel>?, disposable: Disposable?, isScroll: Boolean)
        fun setXboxData(result: MutableList<GameMainModel>?, disposable: Disposable?, isScroll: Boolean)
        fun setSwitchData(result: MutableList<GameMainModel>?, disposable: Disposable?, isScroll: Boolean)
        fun setError(disposable: Disposable?, message: String?)
    }
}