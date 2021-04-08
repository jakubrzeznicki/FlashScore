package com.kuba.flashscore.ui.util.networking

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultConnectivityManager
@Inject
constructor(
    application: Application
) : ConnectivityManager {
    private val connectionLiveData = ConnectionLiveData(application)

    private val networkObserver = Observer<Boolean> { isConnected -> isNetworkAvailable.value = isConnected }

    // observe this in ui
    override val isNetworkAvailable = MutableLiveData<Boolean>()


    override fun registerConnectionObserver() {
        connectionLiveData.observeForever(networkObserver)
    }

    override fun unregisterConnectionObserver() {
        //connectionLiveData.removeObservers(networkObserver)
    }
}