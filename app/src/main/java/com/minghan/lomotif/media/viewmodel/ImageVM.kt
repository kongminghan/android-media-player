package com.minghan.lomotif.media.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.minghan.lomotif.media.api.ImageApi
import com.minghan.lomotif.media.data.Image
import com.minghan.lomotif.media.dataSource.BackgroundThreadExecutor
import com.minghan.lomotif.media.dataSource.image.ImageDataFactory
import javax.inject.Inject

class ImageVM @Inject constructor(
    app: Application,
    imageApi: ImageApi
) : AndroidViewModel(app) {

    private val dataFactory: ImageDataFactory = ImageDataFactory(imageApi = imageApi)

    private val config = PagedList.Config.Builder()
        .setPageSize(100)
        .setInitialLoadSizeHint(150)
        .setEnablePlaceholders(true)
        .build()

    val selectedImage: MutableLiveData<Image> = MutableLiveData()

    val images = LivePagedListBuilder(dataFactory, config)
        .setFetchExecutor(BackgroundThreadExecutor())
        .build()

    val networkState = Transformations.switchMap(dataFactory.imageDataSource) {
        it.networkState
    }

}
