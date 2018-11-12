package com.dev.skh.resellium.Base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.dev.skh.resellium.Board.Model.BoardMainModel
import com.dev.skh.resellium.Board.Sub.InnerBoardMainActivity
import com.dev.skh.resellium.Game.Inner.GameMainCommentActivity
import com.dev.skh.resellium.Game.Model.GameMainModel
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
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputManager?.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun clearAndClose(edit: EditText) {
        edit.text.clear()
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputManager?.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun shortToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun setRvWithoutDeco(rvGame: RecyclerView, layoutManager: LinearLayoutManager): RecyclerView {
        rvGame.isNestedScrollingEnabled = false
        rvGame.layoutManager = layoutManager

        return rvGame
    }

    fun setGameRv(rvGame: RecyclerView, layoutManager: LinearLayoutManager): RecyclerView {
        val decor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        decor.setDrawable(ContextCompat.getDrawable(this, R.drawable.survey_divider)!!)
        rvGame.isNestedScrollingEnabled = false
        rvGame.layoutManager = layoutManager
        rvGame.addItemDecoration(decor)

        return rvGame
    }

    fun setCommentRv(rvGame: RecyclerView, layoutManager: LinearLayoutManager): RecyclerView {
//        val decor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
//        decor.setDrawable(ContextCompat.getDrawable(this, R.drawable.comment_divider)!!)
        rvGame.isNestedScrollingEnabled = false
        rvGame.layoutManager = layoutManager
//        rvGame.addItemDecoration(decor)

        return rvGame
    }

    fun setGridGameRv(rvGame: RecyclerView, layoutManager: GridLayoutManager): RecyclerView {
        rvGame.isNestedScrollingEnabled = false
        rvGame.layoutManager = layoutManager
        val result = Math.round(8 * resources.displayMetrics.density)
        rvGame.addItemDecoration(GridSpacingItemDecoration(2, result, true, 0))
        return rvGame
    }

    fun slideUp(view: View) {
        val animate = TranslateAnimation(
                0f, // fromXDelta
                0f, // toXDelta
                view.height.toFloat(), // fromYDelta
                0f)                // toYDelta
        animate.duration = 250
        animate.fillAfter = true
        view.startAnimation(animate)
    }

    fun slideDown(view: View) {
        val animate = TranslateAnimation(
                0f, // fromXDelta
                0f, // toXDelta
                0f, // fromYDelta
                view.height.toFloat()) // toYDelta
        animate.duration = 250
        animate.fillAfter = true
        view.startAnimation(animate)
    }

    fun setInnerIntent(model: Any?, view: View?) {
        if (model != null) {
            if (model is GameMainModel) {
                gameMainCommentIntent(model, view)
            } else if (model is BoardMainModel) {
                innerBoardMainIntent(model, view)
            }
        }
    }

    private fun gameMainCommentIntent(model: GameMainModel, view: View?) {
        val intent = Intent(view?.context, GameMainCommentActivity::class.java)
        intent.putExtra("data", model)
        startActivity(intent, ActivityOptionsCompat
                .makeSceneTransitionAnimation(this
                        , view?.findViewById(R.id.txt_title) as TextView
                        , "game_title").toBundle())
    }

    private fun innerBoardMainIntent(model: BoardMainModel, view: View?) {
        val intent = Intent(view?.context, InnerBoardMainActivity::class.java)
        intent.putExtra("data", model)
        val p1 = Pair.create(view?.findViewById(R.id.txt_title) as View, "board_title")
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1)
        startActivity(intent, options.toBundle())
    }

    fun showErrorToast() {
        Toast.makeText(this, getString(R.string.error_toast), Toast.LENGTH_SHORT).show()
    }

    fun setBtnDefault(btn: TextView) {
        btn.typeface = Typeface.DEFAULT
        btn.setTextColor(ContextCompat.getColor(this, R.color.middarkGrey))
        btn.background = ContextCompat.getDrawable(this, R.drawable.text_round_white_border_grey)
    }

    fun setBtnAccent(btn: TextView) {
        btn.typeface = Typeface.DEFAULT_BOLD
        btn.setTextColor(ContextCompat.getColor(this, R.color.white))
        btn.background = ContextCompat.getDrawable(this, R.drawable.text_round_secondary_border_grey)

    }

    fun setSnackBar(view: View, text: String) {
        val snackbar = Snackbar
                .make(view, text, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.fabColor))
        val textview = snackbar.view.findViewById(android.support.design.R.id.snackbar_text) as? TextView
        textview?.setTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.show()
    }

    fun setBold(view: TextView) {
        view.setTextColor(ContextCompat.getColor(this, R.color.white))
        view.typeface = Typeface.DEFAULT_BOLD
    }

    fun setDefault(view: TextView) {

        view.setTextColor(ContextCompat.getColor(this, R.color.black))
        view.typeface = Typeface.DEFAULT
    }

}