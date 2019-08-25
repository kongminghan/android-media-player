package com.minghan.lomotif.media.data

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource

class SimpleMusicPlayer(private val context: Context) {

    var player: ExoPlayer? = null

    fun release() {
        player?.release()
        player = null
    }

    fun isPlaying(): Boolean {
        return player?.playbackState ?: 0 == Player.STATE_READY
    }

    fun prepareSource(mediaSource: MediaSource) {
        release()
        setupMedia()
        internalPrepareVideo(mediaSource)
    }

    private fun setupMedia() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                context
            )
        }
        player?.run {
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ALL
        }
    }

    private fun internalPrepareVideo(mediaSource: MediaSource) {
        player?.prepare(mediaSource)
    }
}