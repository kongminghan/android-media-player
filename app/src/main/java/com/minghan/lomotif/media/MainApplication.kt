package com.minghan.lomotif.media

import com.minghan.lomotif.media.dagger.DaggerMainComponent
import com.minghan.lomotif.media.dagger.MainComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class MainApplication : DaggerApplication() {

    lateinit var mainComponent: MainComponent

    companion object {
        private var staticObject: MainApplication? = null

        var instance: MainApplication? = null
            get() {
                return staticObject!!
            }

    }

    override fun onCreate() {
        super.onCreate()
        staticObject = this
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        mainComponent = DaggerMainComponent
            .builder()
            .create(this)
            .build()

        return mainComponent
    }
}
