package com.zeddikus.legohelper.di.featurecomponents

import androidx.lifecycle.ViewModel
import com.zeddikus.legohelper.data.db.SetsRepositoryImpl
import com.zeddikus.legohelper.data.db.DatabaseDao
import com.zeddikus.legohelper.db.RoomDB
import com.zeddikus.legohelper.di.ViewModelKey
import com.zeddikus.legohelper.domain.SetsInteractor
import com.zeddikus.legohelper.domain.SetsInteractorImpl
import com.zeddikus.legohelper.domain.SetsRepository
import com.zeddikus.legohelper.presentation.viewmodel.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [HomeModule.DatabaseModule::class])
interface HomeModule {

    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    @Binds
    fun bindHomeViewModel(impl: HomeViewModel): ViewModel

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
    }
}
