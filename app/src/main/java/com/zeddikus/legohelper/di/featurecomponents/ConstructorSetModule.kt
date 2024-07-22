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
import com.zeddikus.legohelper.presentation.viewmodel.SetViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module(includes = [
    ConstructorSetModule.DatabaseModule::class
])
interface ConstructorSetModule {

    @IntoMap
    @ViewModelKey(SetViewModel::class)
    @Binds
    fun bindSetViewModel(impl: SetViewModel): ViewModel

    @Binds
    fun provideSetInteractor(impl: SetsInteractorImpl): SetsInteractor

    @Binds
    fun provideSetRepository(repository: SetsRepositoryImpl): SetsRepository

    @Binds
    fun provideNetworkInteractor(impl: NetworkInteractorImpl): NetworkInteractor

    @Binds
    fun provideNetworkRepository(repository: NetworkRepositoryImpl): NetworkRepository

    @Module
    class DatabaseModule {

        @Provides
        fun provideProfileDao(database: RoomDB): DatabaseDao {
            return database.databaseDao()
        }

        @Provides
        fun provideApi(retrofit: Retrofit): LegoBrickApi {
            return retrofit.create(LegoBrickApi::class.java)
        }
    }

}
