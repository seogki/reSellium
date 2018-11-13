package com.dev.skh.resellium.Board.Search

import com.dev.skh.resellium.Base.BasePresenter
import com.dev.skh.resellium.Board.Model.BoardMainModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Seogki on 2018. 10. 11..
 */
class BoardMainSearchPresenter(val view: View? = null) : BasePresenter() {


    fun getData(search: String?) {
        if (search != null) {
            disposable = client.getSearchBoardData(search)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe({
                        view?.registerData(it, disposable, false)
                    }, { error ->
                        view?.errorData(disposable, error.message)
                    })
        }
    }

    fun getScrollData(search: String?, id: String?) {
        if(id != null){
            disposable = client.getSearchScrollBoardData(search!!, id)
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

//    fun getKeyWordData(){
//
//            disposable = client.getKeywordBoardData()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .unsubscribeOn(Schedulers.io())
//                    .subscribe({
//                        view?.keyData(it, disposable)
//                    }, { error ->
//                        view?.errorData(disposable, error.message)
//                    })
//
//    }

    interface View {
        fun registerData(board: MutableList<BoardMainModel>?, disposable: Disposable?, isScroll: Boolean)
        fun errorData(disposable: Disposable?, message: String?)
    }
}