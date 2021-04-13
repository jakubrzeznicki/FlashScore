package com.kuba.flashscore.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kuba.flashscore.databinding.ActivityMainBinding
import com.kuba.flashscore.ui.util.networking.ConnectivityManager
import com.kuba.flashscore.ui.util.FlashScoreFragmentFactory
import com.kuba.flashscore.ui.util.FlashScoreFragmentFactoryEntryPoint
import com.kuba.flashscore.ui.util.networking.DefaultConnectivityManager
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var connectivityManager: DefaultConnectivityManager

    @Inject
    lateinit var fragmentFactory: FlashScoreFragmentFactory

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        val entryPoint = EntryPointAccessors.fromActivity(
            this,
            FlashScoreFragmentFactoryEntryPoint::class.java
        )
        supportFragmentManager.fragmentFactory = entryPoint.getFragmentFactory()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setSupportActionBar(binding.toolbarMain)

        setContentView(view)

    }

    override fun onStart() {
        super.onStart()
        connectivityManager.registerConnectionObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterConnectionObserver(this)
    }

}