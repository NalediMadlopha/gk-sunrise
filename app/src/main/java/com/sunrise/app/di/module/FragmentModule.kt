package com.sunrise.app.di.module

import com.sunrise.app.ui.main.WeatherFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): WeatherFragment

}