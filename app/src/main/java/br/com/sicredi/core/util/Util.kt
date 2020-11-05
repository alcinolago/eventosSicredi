package br.com.sicredi.core.util

import java.text.SimpleDateFormat
import java.util.Date

object Util {

    fun getDateTime(s: String): String {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy às hh:mm")
            val netDate = Date(s.toLong() * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }
}
