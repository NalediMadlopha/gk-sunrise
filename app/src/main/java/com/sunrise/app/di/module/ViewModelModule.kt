package com.sunrise.app.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sunrise.app.di.ViewModelFactory
import com.sunrise.app.di.key.ViewModelKey
import com.sunrise.app.ui.main.MainFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @IntoMap
    @Binds
    @ViewModelKey(MainFragmentViewModel::class)
    abstract fun provideMainViewModel(mainFragmentViewModel: MainFragmentViewModel): ViewModel

}