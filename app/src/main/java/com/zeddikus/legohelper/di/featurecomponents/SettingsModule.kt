package com.zeddikus.legohelper.di.featurecomponents

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.zeddikus.legohelper.data.sharedpreferences.SettingsRepositoryImpl
import com.zeddikus.legohelper.domain.SettingsInteractor
import com.zeddikus.legohelper.domain.SettingsInteractorImpl
import com.zeddikus.legohelper.domain.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
class SettingsModule {

    @Provides
    fun provideSettingsInteractor(impl: SettingsInteractorImpl): SettingsInteractor = impl

    @Provides
    fun provideSettings(context: Context): SettingsRepository {
        return provideSettings(context)
    }
}

fun provideSettingsRepository(context: Context): SettingsRepository {
    return SettingsRepositoryImpl(
        context,
        Gson(),
        context.getSharedPreferences(
            "legohelper_prefs",
            Application.MODE_PRIVATE
        )
    )
}