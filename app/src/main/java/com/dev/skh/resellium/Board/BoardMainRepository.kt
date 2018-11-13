package com.dev.skh.resellium.Board

import android.arch.lifecycle.MutableLiveData
import com.dev.skh.resellium.Base.BaseRepository
import com.dev.skh.resellium.Board.Model.BoardMainModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Seogki on 2018. 11. 12..
 */
class BoardMainRepository : BaseRepository() {

    private val msg: MutableLiveData<String> = MutableLiveData()
    private val data: MutableLiveData<MutableList<BoardMainModel>> = MutableLiveData()
    private val liveDisposable: MutableLiveData<Disposable> = MutableLiveData()

    fun getBoardData(id: String?): MutableLiveData<MutableList<BoardMainModel>> {
        if (id == null) {
            disposable = client.getBoardData()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe({
                        data.value = it
                    }, { error ->
                        msg.value = error.message
                    })
            liveDisposable.value = disposable
            return data
        } else {
            disposable = client.getScrollBoardData(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe({
                        data.value = it
                    }, { error ->
                        msg.value = error.message
                    })
            liveDisposable.value = disposable
            return data
        }
    }

    fun getSpinnerData(spinner: String?, id: String?): MutableLiveData<MutableList<BoardMainModel>> {
        if (spinner != null && id == null) {
            disposable = client.getSpinnerBoardData(spinner)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe({
                        data.value = it
                    }, { error ->
                        msg.value = error.message
                    })
            liveDisposable.value = disposable
            return data

        } else if (spinner != null && id != null) {
            disposable = client.getSpinnerScrollBoardData(spinner, id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe({
                        data.value = it
                    }, { error ->
                        msg.value = error.message
                    })
            liveDisposable.value = disposable
            return data
        } else
            return data
    }


    fun getErrorData(): MutableLiveData<String> {
        return msg
    }

    fun getDisposable(): MutableLiveData<Disposable> {
        return liveDisposable
    }
}