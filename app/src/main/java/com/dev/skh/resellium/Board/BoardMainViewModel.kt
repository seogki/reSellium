package com.dev.skh.resellium.Board

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.dev.skh.resellium.Board.Model.BoardMainModel
import io.reactivex.disposables.Disposable


/**
 * Created by Seogki on 2018. 11. 8..
 */
class BoardMainViewModel(application: Application) : AndroidViewModel(application) {
    private val repo by lazy { BoardMainRepository() }

    val boardErrorObservable: LiveData<String>
    val boardDisposableObservable: LiveData<Disposable>
    var boardListObservable: LiveData<MutableList<BoardMainModel>>? = null


    init {
        boardDisposableObservable = repo.getDisposable()
        boardErrorObservable = repo.getErrorData()
    }

    fun setId(spinner: String?, id: String?) {

        boardListObservable = if (spinner == null && id == null)
            repo.getBoardData(null)
        else if (spinner == null && id != null)
            repo.getBoardData(id)
        else if (spinner != null && id == null)
            repo.getSpinnerData(spinner, null)
        else
            repo.getSpinnerData(spinner, id)

    }

}