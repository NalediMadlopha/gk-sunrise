package com.sunrise.app.di.module

import com.sunrise.app.ui.main.WeatherActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun weatherActivity(): WeatherActivity

}