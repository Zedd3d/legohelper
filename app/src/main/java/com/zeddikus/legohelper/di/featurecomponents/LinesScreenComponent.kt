package com.zeddikus.legohelper.di.featurecomponents

import com.zeddikus.legohelper.di.AppComponent
import com.zeddikus.legohelper.di.ScreenComponent
import dagger.Component

@Component(
    dependencies = [AppComponent::class],
    modules = [LinesScreenModule::class]
)
interface LinesScreenComponent : ScreenComponent {
    @Component.Builder
    interface Builder {
        fun build(): LinesScreenComponent
        fun appComponent(appComponent: AppComponent): Builder
    }
}
