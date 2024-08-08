package com.zeddikus.legohelper.di.featurecomponents

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.zeddikus.legohelper.data.db.SetsRepositoryImpl
import com.zeddikus.legohelper.data.db.DatabaseDao
import com.zeddikus.legohelper.data.sharedpreferences.SettingsRepositoryImpl
import com.zeddikus.legohelper.db.RoomDB
import com.zeddikus.legohelper.di.ViewModelKey
import com.zeddikus.legohelper.domain.SetsInteractor
import com.zeddikus.legohelper.domain.SetsInteractorImpl
import com.zeddikus.legohelper.domain.SetsRepository
import com.zeddikus.legohelper.domain.SettingsInteractor
import com.zeddikus.legohelper.domain.SettingsInteractorImpl
import com.zeddikus.legohelper.domain.SettingsRepository
import com.zeddikus.legohelper.presentation.viewmodel.LinesScreenViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [
    LinesScreenModule.DatabaseModule::class
])
interface LinesScreenModule {

    @IntoMap
    @ViewModelKey(LinesScreenViewModel::class)
    @Binds
    fun bindSetViewModel(impl: LinesScreenViewModel): ViewModel

    @Binds
    fun provideSetInteractor(impl: SetsInteractorImpl): SetsInteractor

    @Binds
    fun provideSetRepository(repository: SetsRepositoryImpl): SetsRepository

    @Module
    class DatabaseModule {

        @Provides
        fun provideProfileDao(database: RoomDB): DatabaseDao {
            return database.databaseDao()
        }

        @Provides
        fun provideSettings(context: Context): SettingsRepository {
            return provideSettingsRepository(context)
        }

        @Provides
        fun provideSettingsInteractor(impl: SettingsInteractorImpl): SettingsInteractor = impl
    }

}
