package com.minghan.lomotif.media


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioListener
import com.google.android.exoplayer2.audio.AudioRendererEventListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.minghan.lomotif.media.dagger.Injector
import com.minghan.lomotif.media.dagger.ViewModelFactory
import com.minghan.lomotif.media.data.Music
import com.minghan.lomotif.media.data.SimpleMusicPlayer
import com.minghan.lomotif.media.extension.getViewModel
import com.minghan.lomotif.media.viewmodel.MusicVM
import kotlinx.android.synthetic.main.fragment_music_player.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * A simple [BottomSheetDialogFragment] subclass.
 */
class MusicPlayerFragment : BottomSheetDialogFragment(), CoroutineScope {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val job = SupervisorJob()
    private var musicPlayer: SimpleMusicPlayer? = null
    private var music: Music? = null
    private var musicVM: MusicVM? = null

    companion object {
        const val TAG = "MUSIC_PLAYER"

        fun newInstance(music: Music): MusicPlayerFragment {
            return MusicPlayerFragment().apply {
                this.music = music
            }
        }
    }

    init {
        Injector.get().inject(this)
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        musicVM = activity?.getViewModel(viewModelFactory)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialog ->
            val d = dialog as? BottomSheetDialog
            val bottomSheetInternal =
                d?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            val behavior = BottomSheetBehavior.from(bottomSheetInternal)
            behavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music_player, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlideApp.with(image)
            .load(music?.albumArtUri)
            .placeholder(R.drawable.album_art)
            .apply {
                transform(
                    CenterCrop(),
                    RoundedCorners(8)
                )
            }
            .into(image)

        tv_title.text = music?.title
        tv_subtitle.text = "${music?.artist} | ${music?.albumName}"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startAudio()

        musicVM?.musicMediaSource?.observe(this, Observer {
            it ?: return@Observer

            musicPlayer?.prepareSource(it)

            setEventListener()

            player_control_view.player = musicPlayer?.player
        })
    }

    private fun startAudio() {
        musicPlayer?.release()
        musicPlayer = SimpleMusicPlayer(context!!)

        music?.let {
            musicVM?.requestMusicUri(it)
        }
    }

    private fun setEventListener() {
        musicPlayer?.player?.addListener(
            object : Player.EventListener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    when (playbackState) {
                        ExoPlayer.STATE_ENDED -> {
                            musicPlayer?.player?.playWhenReady = false
                            musicPlayer?.player?.seekTo(0)
                            blast?.hide()
                        }
                    }

                    if (!playWhenReady) {
                        visualizer_container?.background = null
                    } else {
                        visualizer_container?.setBackgroundResource(R.drawable.rectangle_rounded_16dp)
                    }
                }
            }
        )

        musicPlayer?.player?.audioComponent?.addAudioListener(
            object : AudioListener {
                override fun onAudioSessionId(audioSessionId: Int) {
                    try {
                        if (audioSessionId != -1)
                            blast?.setAudioSessionId(audioSessionId)
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }
        )
    }


    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        release()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        release()
    }

    override fun getTheme(): Int {
        return R.style.AppTheme_BottomSheet
    }

    private fun release() {
        blast?.release()
        musicPlayer?.release()
        musicVM?.musicMediaSource?.value = null
    }


}
