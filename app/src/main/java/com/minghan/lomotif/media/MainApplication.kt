package com.minghan.lomotif.media

import com.facebook.drawee.backends.pipeline.Fresco
import com.minghan.lomotif.media.dagger.DaggerMainComponent
import com.minghan.lomotif.media.dagger.MainComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig
import com.facebook.imagepipeline.core.ImagePipelineConfig



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

        val config = ImagePipelineConfig.newBuilder(this)
            .setProgressiveJpegConfig(SimpleProgressiveJpegConfig())
            .setResizeAndRotateEnabledForNetwork(true)
            .setDownsampleEnabled(true)
            .build()

        Fresco.initialize(this, config)
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
