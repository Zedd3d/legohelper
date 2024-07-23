package com.zeddikus.legohelper.di.featurecomponents

import androidx.lifecycle.ViewModel
import com.zeddikus.legohelper.data.db.SetsRepositoryImpl
import com.zeddikus.legohelper.data.db.DatabaseDao
import com.zeddikus.legohelper.data.network.LegoBrickApi
import com.zeddikus.legohelper.data.network.NetworkRepositoryImpl
import com.zeddikus.legohelper.db.RoomDB
import com.zeddikus.legohelper.di.ViewModelKey
import com.zeddikus.legohelper.domain.SetsInteractor
import com.zeddikus.legohelper.domain.SetsInteractorImpl
import com.zeddikus.legohelper.domain.SetsRepository
import com.zeddikus.legohelper.domain.network.NetworkInteractor
import com.zeddikus.legohelper.domain.network.NetworkInteractorImpl
import com.zeddikus.legohelper.domain.network.NetworkRepository
import com.zeddikus.legohelper.presentation.viewmodel.LineDetailsViewModel
import com.zeddikus.legohelper.presentation.viewmodel.LinesScreenViewModel
import com.zeddikus.legohelper.presentation.viewmodel.SetViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module(includes = [
    LineDetailsModule.DatabaseModule::class
])
interface LineDetailsModule {

    @IntoMap
    @ViewModelKey(LineDetailsViewModel::class)
    @Binds
    fun bindSetViewModel(impl: LineDetailsViewModel): ViewModel

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
