package com.kuba.flashscore.ui.util.networking

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

interface ConnectivityManager {
    val isNetworkAvailable: MutableLiveData<Boolean>
    fun registerConnectionObserver()
    fun unregisterConnectionObserver()
}