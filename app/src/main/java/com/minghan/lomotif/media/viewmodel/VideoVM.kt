package com.minghan.lomotif.media.viewmodel

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.minghan.lomotif.media.BuildConfig
import com.minghan.lomotif.media.data.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoVM @Inject constructor(private val app: Application) : AndroidViewModel(app) {

    val videos = MutableLiveData<List<Video>>()
    val videoMediaSource = MutableLiveData<MediaSource>()

    fun videoFiles(context: Context): Job = viewModelScope.launch(Dispatchers.IO) {
        // Initialize an empty mutable list of video
        val list: MutableList<Video> = mutableListOf()

        // Get the external storage media store audio uri
        val uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        // Sort the videos
        val sortOrder = MediaStore.Video.Media.TITLE + " ASC"

        // Query the external storage for video files
        val cursor: Cursor? = context.contentResolver.query(
            uri, // Uri
            null, // Projection
            null, // Selection
            null, // Selection arguments
            sortOrder // Sort order
        )

        // If query result is not empty
        if (cursor != null && cursor.moveToFirst()) {
            val id: Int = cursor.getColumnIndex(MediaStore.Video.Media._ID)
            val title: Int = cursor.getColumnIndex(MediaStore.Video.Media.TITLE)
            val path: Int = cursor.getColumnIndex(MediaStore.Video.Media.DATA)
            val duration: Int = cursor.getColumnIndex(MediaStore.Video.Media.DURATION)

            // Now loop through the video files
            do {
                val videoId: Long = cursor.getLong(id)
                val videoTitle: String = cursor.getString(title)
                val videoPath: String = cursor.getString(path)
                val videoMs = cursor.getLong(duration)

                val thumb = MediaStore.Video.Thumbnails.getThumbnail(
                    context.contentResolver,
                    videoId,
                    MediaStore.Images.Thumbnails.MINI_KIND,  // 512 x 384
                    null
                )

                val minutes = videoMs / 1000 / 60
                val seconds = videoMs / 1000 % 60

                // Add the current video to the list
                list.add(
                    Video(
                        id = videoId,
                        title = videoTitle,
                        path = videoPath,
                        duration = "${minutes}m ${seconds}s",
                        albumThumb = thumb
                    )
                )
            } while (cursor.moveToNext())
        }

        // Finally, return the video files list
        videos.postValue(list)
    }

    fun requestVideoUri(video: Video) {
        val uri = Uri.parse(video.path)
        val mediaSource = mediaSources(uri)
        videoMediaSource.postValue(mediaSource)
    }

    private fun mediaSources(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(
            app.applicationContext,
            Util.getUserAgent(app.applicationContext, BuildConfig.APPLICATION_ID)
        )

        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
    }
}