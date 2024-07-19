package com.zeddikus.legohelper.di.featurecomponents

import com.zeddikus.legohelper.di.AppComponent
import com.zeddikus.legohelper.di.ScreenComponent
import dagger.Component

@Component(
    dependencies = [AppComponent::class],
    modules = [HomeModule::class]
)
interface HomeComponent : ScreenComponent {
    @Component.Builder
    interface Builder {
        fun build(): HomeComponent
        fun appComponent(appComponent: AppComponent): Builder
    }
}
