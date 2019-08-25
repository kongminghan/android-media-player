package com.minghan.lomotif.media.dagger

import com.minghan.lomotif.media.MainApplication

class Injector private constructor() {
    companion object {
        fun get(): MainComponent =
            MainApplication.instance!!.mainComponent
    }
}