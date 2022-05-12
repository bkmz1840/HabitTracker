package com.doubletapp.habittracker.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NetworkMonitoringUtil(
    context: Context
) : ConnectivityManager.NetworkCallback() {
    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()
    private val connectivityManager: ConnectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    private val _networkStatus: MutableLiveData<Boolean> = MutableLiveData()
    val networkStatus: LiveData<Boolean> = _networkStatus

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        _networkStatus.postValue(true)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        _networkStatus.postValue(false)
    }

    fun registerNetworkCallbackEvents() {
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    private fun getNetworkStatus(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }

    fun checkNetworkStatus() {
        try {
            val status = getNetworkStatus()
            _networkStatus.postValue(status)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}