package com.minghan.lomotif.media.dagger

import android.app.Application
import com.minghan.lomotif.media.MainApplication
import com.minghan.lomotif.media.MusicPlayerFragment
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityModule::class,
        ViewModelModule::class,
        ApiModule::class
    ]
)
interface MainComponent : AndroidInjector<MainApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun create(app: Application): Builder

        fun build(): MainComponent
    }

    fun inject(musicPlayer: MusicPlayerFragment)

}
