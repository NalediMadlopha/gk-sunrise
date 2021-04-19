package com.sunrise.app.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sunrise.app.R
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class WeatherActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_activity)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}