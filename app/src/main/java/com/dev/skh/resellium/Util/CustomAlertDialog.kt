package com.dev.skh.resellium.Util

import android.app.AlertDialog
import android.content.Context

/**
 * Created by Seogki on 2018. 11. 13..
 */
class CustomAlertDialog(context: Context, listener: DialogListener?, title: String, msg: String, theme: Int) {

    private val dialog: AlertDialog

    init {
        dialog = AlertDialog.Builder(context, theme)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                    listener?.onYesDialog()

                }.setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                    listener?.onNoDialog()
                }
                .show()
    }

    interface DialogListener {
        fun onYesDialog()
        fun onNoDialog()
    }
}