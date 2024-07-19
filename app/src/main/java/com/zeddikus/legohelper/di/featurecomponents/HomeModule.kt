package com.zeddikus.legohelper.di.featurecomponents

import androidx.lifecycle.ViewModel
import com.zeddikus.legohelper.data.SetsRepositoryImpl
import com.zeddikus.legohelper.data.db.DatabaseDao
import com.zeddikus.legohelper.db.RoomDB
import com.zeddikus.legohelper.di.ViewModelKey
import com.zeddikus.legohelper.domain.LoadSetsUseCase
import com.zeddikus.legohelper.domain.LoadSetsUseCaseImpl
import com.zeddikus.legohelper.domain.SetsRepository
import com.zeddikus.legohelper.presentation.viewmodel.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module(includes = [HomeModule.DatabaseModule::class])
interface HomeModule {

    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    @Binds
    fun bindAuthorizationViewModel(impl: HomeViewModel): ViewModel

    @Binds
    fun provideAuthorisationUseCase(useCase: LoadSetsUseCaseImpl): LoadSetsUseCase

    @Binds
    fun provideAuthorisationRepository(repository: SetsRepositoryImpl): SetsRepository

    @Module
    class DatabaseModule {

        @Provides
        fun provideProfileDao(database: RoomDB): DatabaseDao {
            return database.databaseDao()
        }
    }
}
