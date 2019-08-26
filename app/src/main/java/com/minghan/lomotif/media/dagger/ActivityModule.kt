package com.minghan.lomotif.media.dagger

import com.minghan.lomotif.media.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun musicFragment(): MusicFragment

    @ContributesAndroidInjector
    abstract fun movieFragment(): MovieFragment

    @ContributesAndroidInjector
    abstract fun videoPlayerFragment(): VideoPlayerFragment

    @ContributesAndroidInjector
    abstract fun galleryFragment(): GalleryFragment

}
