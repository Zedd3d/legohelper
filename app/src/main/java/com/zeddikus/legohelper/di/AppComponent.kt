package com.zeddikus.legohelper.di

import android.app.Application
import android.content.Context
import com.zeddikus.legohelper.db.RoomDB
import com.zeddikus.legohelper.db.RoomDBModule
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit

@Component(
    modules = [
        NetworkModule::class,
        RoomDBModule::class,
        ContextModule::class,
    ]
)
interface AppComponent : DIComponent {
    val room: RoomDB
    val context: Context
    val retrofit: Retrofit

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun app(app: Application): Builder
    }
}

interface DIComponent

// Эта сущность нужна чтобы хранить AppComponent в памяти и подсовывать в наши ScreenComponent'ы
object AppComponentHolder : DataBasedComponentHolder<AppComponent, Application>() {
    override val mode: ComponentHolderMode = ComponentHolderMode.GLOBAL_SINGLETON
    override fun buildComponent(data: Application): AppComponent =
        DaggerAppComponent.builder().app(data).build()
}
