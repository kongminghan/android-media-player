package com.minghan.lomotif.media.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.minghan.lomotif.media.api.ImageApi
import com.minghan.lomotif.media.dataSource.BackgroundThreadExecutor
import com.minghan.lomotif.media.dataSource.image.ImageDataFactory
import javax.inject.Inject

class ImageVM @Inject constructor(
    private val app: Application,
    private val imageApi: ImageApi
) : AndroidViewModel(app) {

    private val dataFactory: ImageDataFactory = ImageDataFactory(imageApi = imageApi)

    private val config = PagedList.Config.Builder()
        .setPageSize(20)
        .setInitialLoadSizeHint(80)
        .setEnablePlaceholders(true)
        .build()


    val images = LivePagedListBuilder(dataFactory, config)
        .setFetchExecutor(BackgroundThreadExecutor())
        .build()

}
