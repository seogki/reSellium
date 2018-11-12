package com.dev.skh.resellium.Board

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.dev.skh.resellium.Base.BasePresenter
import com.dev.skh.resellium.Board.Model.BoardMainModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Seogki on 2018. 11. 12..
 */
class BoardMainRepository : BasePresenter() {


    fun getBoardData(): LiveData<MutableList<BoardMainModel>> {
        val data: MutableLiveData<MutableList<BoardMainModel>> = MutableLiveData()
        disposable = client.getBoardData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({
                    data.value = it

                }, { error ->

                })

        return data
    }
}