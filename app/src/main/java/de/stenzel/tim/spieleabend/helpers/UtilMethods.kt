package de.stenzel.tim.spieleabend.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.time.*
import java.util.*

fun formatTimstampToDateString(unixTimestamp: Long) : String {

    val date = Date(unixTimestamp*1000)
    val format = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)

    return format.format(date)
}

fun timestampToLocalDate(unixTimestamp: Long): LocalDate {
    return Instant.ofEpochSecond(unixTimestamp).atZone(ZoneId.systemDefault()).toLocalDate()
}

fun timestampToLocalTime(unixTimestamp: Long) : LocalTime {
    return Instant.ofEpochSecond(unixTimestamp).atZone(ZoneId.systemDefault()).toLocalTime()
}

fun timestampToLocalDateTime(unixTimestamp: Long) : LocalDateTime {
    return Instant.ofEpochSecond(unixTimestamp).atZone(ZoneId.systemDefault()).toLocalDateTime()
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                return true
            }
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                return true
            }
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                return true
            }
        }
    }
    return false
}

//kotlin extension functions
fun Fragment.showToast(message: String) {
    Toast.makeText(
        this.requireContext(),
        message,
        Toast.LENGTH_LONG
    ).show()
}

fun EditText.clearError() {
    error = null
}

















