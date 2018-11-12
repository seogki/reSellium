package com.dev.skh.resellium.Board

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.dev.skh.resellium.Board.Model.BoardMainModel


/**
 * Created by Seogki on 2018. 11. 8..
 */
class BoardMainViewModel(application: Application) : AndroidViewModel(application) {

    private var boardListObservable: LiveData<MutableList<BoardMainModel>>
    private val repo by lazy { BoardMainRepository() }

    init {
        boardListObservable = repo.getBoardData()
    }

    fun getBoardListObservable(): LiveData<MutableList<BoardMainModel>> {
        return boardListObservable
    }
}