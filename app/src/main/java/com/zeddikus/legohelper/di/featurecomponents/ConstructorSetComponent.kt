package com.zeddikus.legohelper.di.featurecomponents

import com.zeddikus.legohelper.di.AppComponent
import com.zeddikus.legohelper.di.ScreenComponent
import dagger.Component

@Component(
    dependencies = [AppComponent::class],
    modules = [ConstructorSetModule::class]
)
interface ConstructorSetComponent : ScreenComponent {
    @Component.Builder
    interface Builder {
        fun build(): ConstructorSetComponent
        fun appComponent(appComponent: AppComponent): Builder
    }
}
