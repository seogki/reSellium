package com.dev.skh.resellium.Base

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.support.annotation.IdRes
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.dev.skh.resellium.Board.BoardMainActivity
import com.dev.skh.resellium.Board.Model.BoardMainModel
import com.dev.skh.resellium.Board.Search.BoardMainSearchActivity
import com.dev.skh.resellium.Board.Sub.InnerBoardMainActivity
import com.dev.skh.resellium.Game.GameMainActivity
import com.dev.skh.resellium.Game.Inner.GameMainCommentActivity
import com.dev.skh.resellium.Game.Model.GameMainModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.User.UserMainActivity


/**
 * Created by Seogki on 2018. 6. 7..
 */
open class BaseFragment : Fragment() {


    private var progressBar: ProgressBar? = null

    fun Fragment.beginNewActivity(intent: Intent) {
        startActivity(intent)
    }


    fun addFragment(activity: FragmentActivity?, @IdRes frameId: Int, fragment: Fragment, AllowStateloss: Boolean, backstack: Boolean, tag: String) {

        val transaction = activity?.supportFragmentManager?.beginTransaction()?.add(frameId, fragment, tag)

        if (backstack) {
            transaction?.addToBackStack(fragment.tag)
        }

        if (AllowStateloss)
            transaction?.commitAllowingStateLoss()
        else
            transaction?.commit()
    }

    fun Fragment.beginActivity(intent: Intent) {

        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }


    fun closeKeyboard() {
        val inputManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

    }

    fun clearAndClose(edit: EditText) {
        edit.text.clear()
        val inputManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun openKeyboard() {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }


    fun setGameRv(rvGame: RecyclerView, layoutManager: LinearLayoutManager): RecyclerView {
        val decor = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        decor.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.survey_divider)!!)
        rvGame.isNestedScrollingEnabled = false
        rvGame.layoutManager = layoutManager
        rvGame.addItemDecoration(decor)

        return rvGame
    }

//    fun setGridGameRv(rvGame: RecyclerView, layoutManager: GridLayoutManager): RecyclerView {
//        rvGame.isNestedScrollingEnabled = false
//        rvGame.layoutManager = layoutManager
//
//        val result = Math.round(8 * resources.displayMetrics.density)
//
//        rvGame.addItemDecoration(GridSpacingItemDecoration(2, result, true, 0))
//
//        return rvGame
//    }


    fun setBaseProgressBar(progressBar: ProgressBar) {
        this.progressBar = progressBar
    }

    fun setProgressbarGone() {
        progressBar?.visibility = View.GONE
    }

    fun setProgressbarVisible() {
        progressBar?.visibility = View.VISIBLE
    }

    fun setBtnDefault(btn: TextView) {
        btn.setTextColor(ContextCompat.getColor(context!!, R.color.middarkGrey))
        btn.typeface = Typeface.DEFAULT
        btn.background = ContextCompat.getDrawable(context!!, R.drawable.text_round_white_border_grey)
    }

    fun setBtnAccent(btn: TextView) {
        btn.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        btn.typeface = Typeface.DEFAULT_BOLD
        btn.background = ContextCompat.getDrawable(context!!, R.drawable.text_round_secondary_border_grey)

    }

    fun setInnerIntent(model: Any?, view: View?) {

        model?.let {
            when (it) {
                is GameMainModel -> gameMainCommentIntent(it, view)
                is BoardMainModel -> innerBoardMainIntent(it, view)
                else -> return
            }
        }

    }

    private fun gameMainCommentIntent(model: GameMainModel, view: View?) {
        val intent = Intent(view?.context, GameMainCommentActivity::class.java)
        intent.putExtra("data", model)
        startActivity(intent, ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity!!
                        , view!!
                        , "game_title").toBundle())
    }

    private fun innerBoardMainIntent(model: BoardMainModel, view: View?) {
        val intent = Intent(view?.context, InnerBoardMainActivity::class.java)
        intent.putExtra("data", model)
        val p1 = android.support.v4.util.Pair.create(view!!, "board_title")
//                val p2 = android.support.v4.util.Pair.create(view.findViewById(R.id.txt_grade) as View, "board_grade")
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, p1)
        startActivity(intent, options.toBundle())
    }

    fun setTabInnerIntent(model: BoardMainModel, view: View?) {
        val intent = Intent(view?.context, InnerBoardMainActivity::class.java)
        intent.putExtra("data", model)
        startActivity(intent, ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity!!
                        , view!!
                        , "board_title").toBundle())
    }

    fun showErrorToast() {
        Toast.makeText(context, getString(R.string.error_toast), Toast.LENGTH_SHORT).show()
    }

    fun searchBoardIntent() {
        startActivity(Intent(context!!, BoardMainSearchActivity::class.java))
    }

    fun settingIntent() {
        startActivity(Intent(context!!, UserMainActivity::class.java))
    }

    fun boardMainIntent() {
        beginActivity(Intent(context!!, BoardMainActivity::class.java))
    }

    fun gameMainIntent() {
        beginActivity(Intent(context!!, GameMainActivity::class.java))
    }
}