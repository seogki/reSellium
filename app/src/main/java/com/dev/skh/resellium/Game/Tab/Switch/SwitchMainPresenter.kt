package com.dev.skh.resellium.Game.Tab.Switch

import com.dev.skh.resellium.Base.BasePresenter
import com.dev.skh.resellium.Game.Model.SwitchMainModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Seogki on 2018. 9. 28..
 */
class SwitchMainPresenter(val view: View? = null) : BasePresenter(){


    fun addData() {
        disposable = client.getSwitchData()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.updateData(it, disposable, false) }
                        , { view?.errorUpdateData(disposable, it.message) })
    }

    fun scrollData(id: String?) {
        if (id != null) {
            disposable = client.getScrollswitchData(id)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ view?.updateData(it, disposable, true) }
                            , { view?.errorUpdateData(disposable, it.message) })
        }
    }

    fun checkSpinnerData(a1: String, a2: String) {

        disposable = client.getSpinnerswitchData(a1, a2)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.updateSpinnerData(it, disposable, true) }
                        , { view?.errorUpdateData(disposable, it.message) })
    }

    fun checkSpinnerScrollData(a1: String, a2: String, id: String?) {
        if (id != null) {

            disposable = client.getSpinnerScrollswitchData(a1, a2, id)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ view?.updateSpinnerData(it, disposable, true) }
                            , { view?.errorUpdateData(disposable, it.message) })
        }
    }

    fun spinnerOne() {
        Thread {
            val arr = mutableListOf<String>()
            arr.add("")
            arr.add("전체")
            arr.add("신품")
            arr.add("중고")
            view?.spinner1(arr)
        }.start()

    }

    fun spinnerTwo() {
        Thread {
            val arr = mutableListOf<String>()
            arr.add("")
            arr.add("전체")
            arr.add("매입")
            arr.add("매각")

            view?.spinner2(arr)
        }.start()

    }

    interface View {

        fun updateData(arr: MutableList<SwitchMainModel>?, disposable: Disposable?, isScroll: Boolean)
        fun updateSpinnerData(result: MutableList<SwitchMainModel>?, disposable: Disposable?, isScroll: Boolean)
        fun errorUpdateData(disposable: Disposable?, message: String?)
        fun spinner1(arr1: MutableList<String>)
        fun spinner2(arr2: MutableList<String>)
    }
}