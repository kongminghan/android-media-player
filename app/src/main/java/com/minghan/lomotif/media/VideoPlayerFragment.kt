package com.minghan.lomotif.media


import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.minghan.lomotif.media.dagger.ViewModelFactory
import com.minghan.lomotif.media.data.SimpleMediaPlayer
import com.minghan.lomotif.media.data.Video
import com.minghan.lomotif.media.extension.getViewModel
import com.minghan.lomotif.media.viewmodel.VideoVM
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.fragment_video_player.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * A simple [DaggerDialogFragment] subclass.
 */
class VideoPlayerFragment : DaggerDialogFragment(), CoroutineScope {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val job = SupervisorJob()
    private var videoVM: VideoVM? = null
    private var video: Video? = null
    private var videoPlayer: SimpleMediaPlayer? = null

    companion object {
        const val TAG = "VIDEO_PLAYER"

        fun newInstance(video: Video): VideoPlayerFragment {
            return VideoPlayerFragment().apply {
                this.video = video
            }
        }
    }


    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_player, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoVM = activity?.getViewModel(viewModelFactory)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            dismiss()
        }

        player_view?.setControllerVisibilityListener {
            if (it == 0) {
                toolbar.visibility = View.VISIBLE
            } else {
                toolbar.visibility = View.GONE
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // full height dialog
        dialog?.window?.apply {
            setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            statusBarColor = Color.TRANSPARENT
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP)
        }

        startVideo()

        videoVM?.videoMediaSource?.observe(this, Observer {
            it ?: return@Observer

            videoPlayer?.prepareSource(it)

            setEventListener()
        })
    }

    private fun startVideo() {
        videoPlayer?.release()
        videoPlayer = SimpleMediaPlayer(context!!, player_view)

        video?.let {
            videoVM?.requestVideoUri(it)
        }
    }

    private fun setEventListener() {
        videoPlayer?.player?.addListener(
            object : Player.EventListener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    when (playbackState) {
                        ExoPlayer.STATE_READY -> {
                            loading?.visibility = View.GONE
                        }
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

    override fun onPause() {
        super.onPause()
        videoPlayer?.player?.playWhenReady = false
    }

    override fun getTheme(): Int {
        return R.style.AppTheme_VideoDialog
    }

    private fun release() {
        videoPlayer?.release()
        videoVM?.videoMediaSource?.value = null
    }

}
