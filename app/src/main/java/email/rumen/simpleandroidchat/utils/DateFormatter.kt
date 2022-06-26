package email.rumen.simpleandroidchat.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DateFormatter @Inject constructor() {

    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("EEEE HH:mm")

    fun formatDate(timestamp: Long): String {
        return formatter.format(Date(timestamp))
    }
}
