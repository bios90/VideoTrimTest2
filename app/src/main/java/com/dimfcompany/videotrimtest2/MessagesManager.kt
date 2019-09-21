package com.dimfcompany.videotrimtest2

import android.app.AlertDialog
import android.content.DialogInterface


class MessagesManager(val activity: ActMain)
{
    fun show_too_long_dialog(okLambda: () -> Unit)
    {
        show_dialog("", "Длина видео больше 30 секунд, обрезать видео?", "Обрезать", okLambda)
    }

    fun show_dialog(title: String, messge: String, okText: String, okLambda: () -> Unit)
    {
        AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(messge)
                .setPositiveButton(okText, DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    okLambda()
                })
                .setNegativeButton("Отмена", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }
}