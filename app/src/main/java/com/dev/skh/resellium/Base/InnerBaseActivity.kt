package com.dev.skh.resellium.Base

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.GridSpacingItemDecoration

/**
 * Created by Seogki on 2018. 10. 17..
 */
@SuppressLint("Registered")
open class InnerBaseActivity : AppCompatActivity() {

    private var progressBar: ProgressBar? = null

    fun setBaseProgressBar(progressBar: ProgressBar) {
        this.progressBar = progressBar
    }

    fun setProgressbarGone() {
        progressBar?.visibility = View.GONE
    }

    fun setProgressbarVisible() {
        progressBar?.visibility = View.VISIBLE
    }

    fun closeKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun clearAndClose(edit: EditText) {
        edit.text.clear()
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun shortToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun setRvWithoutDeco(rvGame: RecyclerView, layoutManager: LinearLayoutManager): RecyclerView {
        rvGame.isNestedScrollingEnabled = false
        rvGame.layoutManager = layoutManager

        return rvGame
    }

    fun setGameRv(rvGame: RecyclerView, layoutManager: LinearLayoutManager): RecyclerView {
        val decor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        decor.setDrawable(ContextCompat.getDrawable(this, R.drawable.survey_divder)!!)
        rvGame.isNestedScrollingEnabled = false
        rvGame.layoutManager = layoutManager
        rvGame.addItemDecoration(decor)

        return rvGame
    }

    fun setCommentRv(rvGame: RecyclerView, layoutManager: LinearLayoutManager): RecyclerView {
        val decor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        decor.setDrawable(ContextCompat.getDrawable(this, R.drawable.comment_divder)!!)
        rvGame.isNestedScrollingEnabled = false
        rvGame.layoutManager = layoutManager
        rvGame.addItemDecoration(decor)

        return rvGame
    }

    fun setGridGameRv(rvGame: RecyclerView, layoutManager: GridLayoutManager) : RecyclerView {
        rvGame.isNestedScrollingEnabled = false
        rvGame.layoutManager = layoutManager
        val result = Math.round(8 * resources.displayMetrics.density)
        rvGame.addItemDecoration(GridSpacingItemDecoration(2, result, true, 0))

        return rvGame
    }
}