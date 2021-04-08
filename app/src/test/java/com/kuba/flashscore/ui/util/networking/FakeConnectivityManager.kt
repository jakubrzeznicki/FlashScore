package com.kuba.flashscore.ui.util.networking

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import javax.inject.Singleton

@Singleton
class FakeConnectivityManager : ConnectivityManager {

    var isObserverRegistered: Boolean = false

    override val isNetworkAvailable = MutableLiveData<Boolean>()

    fun setNetworkAvailable(boolean: Boolean) {
        isNetworkAvailable.value = boolean
    }

    override fun registerConnectionObserver() {
        isObserverRegistered = true
    }

    override fun unregisterConnectionObserver() {
        isObserverRegistered = false
    }
}