package com.zeddikus.legohelper.di.featurecomponents

import com.zeddikus.legohelper.di.AppComponent
import com.zeddikus.legohelper.di.ScreenComponent
import dagger.Component

@Component(
    dependencies = [AppComponent::class],
    modules = [LineDetailsModule::class]
)
interface LineDetailsComponent : ScreenComponent {
    @Component.Builder
    interface Builder {
        fun build(): LineDetailsComponent
        fun appComponent(appComponent: AppComponent): Builder
    }
}
