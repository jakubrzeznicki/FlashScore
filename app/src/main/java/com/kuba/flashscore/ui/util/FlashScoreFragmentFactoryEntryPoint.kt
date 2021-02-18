package com.kuba.flashscore.ui.util

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface FlashScoreFragmentFactoryEntryPoint {
    fun getFragmentFactory() : FlashScoreFragmentFactory
}