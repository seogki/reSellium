package com.dev.skh.resellium.Base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.databinding.BindingAdapter
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.KeyboardUtils
import com.dev.skh.resellium.Util.UtilMethod
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Seogki on 2018. 11. 8..
// */
object BaseBindingKotlin {

    @BindingAdapter("keyboardDetect")
    @JvmStatic
    fun View.keyboardDetect(data: String?) {

        val context = setContext(context) ?: return

        val activity = UtilMethod.getActivity(context) ?: return
        val weakReference = WeakReference(activity)


        if (!activity.isFinishing || !activity.isDestroyed)
            KeyboardUtils.addKeyboardToggleListener(weakReference) { isVisible ->
                visibility = if (isVisible) View.GONE else View.VISIBLE
            }
        else
            KeyboardUtils.removeAllKeyboardToggleListeners()

    }

    @BindingAdapter("sellCheck")
    @JvmStatic
    fun TextView.sellcheck(result: String?) {
        val context = setContext(context) ?: return

        if (result == null)
            return

        when {
            result.contains("매입") -> setTextColor(ContextCompat.getColor(context, R.color.accentColor))
            else -> setTextColor(ContextCompat.getColor(context, R.color.fabColor))
        }

        text = result
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("moneyCheck")
    @JvmStatic
    fun TextView.moneycheck(result: String?) {
        val context = setContext(context) ?: return

        if (result == null)
            return

        UtilMethod.currencyFormat(result).let { text = it+"원" }
    }

    @BindingAdapter("soldCheck")
    @JvmStatic
    fun TextView.soldCheck(result: String?) {
        val context = setContext(context) ?: return
        if (result == null)
            return

        when {
            result.contains("매입") -> setTextColor(ContextCompat.getColor(context, R.color.fabColor))
            else -> setTextColor(ContextCompat.getColor(context, R.color.accentColor))
        }

        text = result
    }

    @BindingAdapter("timeAgo")
    @JvmStatic
    fun TextView.timeAgo(result: String?) {
        val context = setContext(context) ?: return

        if (result == null)
            return

        if (!result.isEmpty()) {
            val cal = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            try {
                cal.time = sdf.parse(result)
                val data = UtilMethod.formatTimeString(cal.timeInMillis, result)
                text = data
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("setComment")
    @JvmStatic
    fun TextView.setComment(result: String?) {
        val context = setContext(context) ?: return

        if (result == null)
            return


        text = when {
            result.isEmpty() -> ""
            result == "0" -> ""
            else -> "($result)"
        }


    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("setGrade")
    @JvmStatic
    fun TextView.setGrade(result: String?) {
        val context = setContext(context) ?: return
        if (result == null)
            return

        text = when {
            !result.isEmpty() -> result + "점"
            else -> ""
        }

    }

    private fun setContext(context: Context?): Context? {
        return if (context == null) {
            null
        } else if (context is Activity) {
            if (context.isFinishing || context.isDestroyed) {
                null
            } else {
                context
            }
        } else {
            context
        }

    }

}