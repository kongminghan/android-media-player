package com.minghan.lomotif.media.dataSource.image

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.minghan.lomotif.media.api.ImageApi
import com.minghan.lomotif.media.constant.Constant.Companion.FAILED
import com.minghan.lomotif.media.constant.Constant.Companion.LOADED
import com.minghan.lomotif.media.constant.Constant.Companion.LOADING
import com.minghan.lomotif.media.data.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageDataFactory(
    private val imageApi: ImageApi
) : DataSource.Factory<Int, Image>() {

    val imageDataSource = MutableLiveData<ImageDataSource>()

    override fun create(): DataSource<Int, Image> {
        val dataSource = ImageDataSource()
        imageDataSource.postValue(dataSource)
        return dataSource
    }

    inner class ImageDataSource :
        PageKeyedDataSource<Int, Image>() {
        val networkState: MutableLiveData<Int> = MutableLiveData()
        private var nextPage = true

        override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Int, Image>
        ) {
            GlobalScope.launch(Dispatchers.Default) {
                val data = getImages(1)

                callback.onResult(
                    data ?: listOf(),
                    null,
                    if (nextPage) {
                        2
                    } else {
                        null
                    }
                )
            }
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Image>) {
            GlobalScope.launch(Dispatchers.Default) {
                callback.onResult(
                    getImages(params.key) ?: listOf(),
                    if (nextPage) {
                        params.key + 1
                    } else {
                        null
                    }
                )
            }
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Image>) {}

        private suspend fun getImages(page: Int): List<Image>? {
            networkState.postValue(LOADING)
            return try {
                val response = withContext(Dispatchers.IO) {
                    imageApi.getImages(page).execute()
                }

                if (response.isSuccessful) {
                    networkState.postValue(LOADED)
                    response.body()?.hits
                } else {
                    if (response.code() == 400) {
                        nextPage = response.code() != 400
                    } else {
                        networkState.postValue(response.code())
                    }
                    null
                }
            } catch (e: Throwable) {
                networkState.postValue(FAILED)
                null
            }
        }
    }
}