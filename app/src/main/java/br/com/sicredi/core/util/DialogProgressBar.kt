package br.com.sicredi.core.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import br.com.sicredi.R

class DialogProgressBar {

    companion object {

        private lateinit var dialog: Dialog

        fun show(context: Context) {

            dialog = AlertDialog.Builder(context)
                .setView(R.layout.dialog_loading)
                .setCancelable(false)
                .show()

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        fun dismiss() {
            if (!this::dialog.isInitialized) return

            dialog.dismiss()
        }
    }
}
