package com.minghan.lomotif.media.viewmodel

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minghan.lomotif.media.data.Music
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class MusicVM @Inject constructor() : ViewModel() {

    val musics = MutableLiveData<List<Music>>()

    fun musicFiles(context: Context): Job = viewModelScope.launch(Dispatchers.IO) {
        // Initialize an empty mutable list of music
        val list: MutableList<Music> = mutableListOf()

        // Get the external storage media store audio uri
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        // val uri: Uri = MediaStore.Files.getContentUri("external")

        // IS_MUSIC : Non-zero if the audio file is music
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"

        // Sort the musics
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        //val sortOrder = MediaStore.Audio.Media.TITLE + " DESC"

        // Query the external storage for music files
        val cursor: Cursor? = context.contentResolver.query(
            uri, // Uri
            null, // Projection
            selection, // Selection
            null, // Selection arguments
            sortOrder // Sort order
        )

        // If query result is not empty
        if (cursor != null && cursor.moveToFirst()) {
            val id: Int = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val title: Int = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artist: Int = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val album: Int = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
            val path: Int = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val albumId: Int = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            val duration: Int = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)

            val artworkUri = Uri
                .parse("content://media/external/audio/albumart")

            // Now loop through the music files
            do {
                val audioId: Long = cursor.getLong(id)
                val audioTitle: String = cursor.getString(title)
                val audioArtist: String = cursor.getString(artist)
                val audioAlbum: String = cursor.getString(album)
                val audioPath: String = cursor.getString(path)
                val audioAlbumId: Long = cursor.getLong(albumId)
                val audioAlbumArtUri = ContentUris.withAppendedId(artworkUri, audioAlbumId)
                val audioMs = cursor.getLong(duration)

                val minutes = audioMs / 1000 / 60
                val seconds = audioMs / 1000 % 60

                // Add the current music to the list
                list.add(
                    Music(
                        id = audioId,
                        title = audioTitle,
                        artist = audioArtist,
                        albumName = audioAlbum,
                        albumArtUri = audioAlbumArtUri,
                        path = audioPath,
                        duration = "${minutes}m ${seconds}s"
                    )
                )
            } while (cursor.moveToNext())
        }

        // Finally, return the music files list
        musics.postValue(list)
    }
}