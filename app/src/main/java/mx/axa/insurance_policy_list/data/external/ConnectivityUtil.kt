package mx.axa.insurance_policy_list.data.external

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

class ConnectivityUtil(val context: Context) {
    fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true) {
            Log.e("CONNECTIVITY", "TRANSPORT_CELULAR")
            return true
        } else if (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {
            Log.e("CONNECTIVITY", "TRANSPORT_WIFI")
            return true
        }
        Log.e("CONNECTIVITY", "NO CONNECTION INTERNET")
        return false
    }
}