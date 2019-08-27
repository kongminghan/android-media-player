package com.minghan.lomotif.media.data

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerView

class SimpleMediaPlayer(
    private val context: Context,
    private val playerView: PlayerView? = null
) {

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
        internalPrepareMedia(mediaSource)
    }

    private fun setupMedia() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                context
            )
        }
        player?.run {
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_OFF
        }

        playerView?.player = player
    }

    private fun internalPrepareMedia(mediaSource: MediaSource) {
        player?.prepare(mediaSource)
    }
}