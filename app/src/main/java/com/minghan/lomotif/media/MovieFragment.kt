package com.minghan.lomotif.media


import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.minghan.lomotif.media.adapter.VideoAdapter
import com.minghan.lomotif.media.dagger.ViewModelFactory
import com.minghan.lomotif.media.extension.checkPermissions
import com.minghan.lomotif.media.extension.getViewModel
import com.minghan.lomotif.media.viewmodel.VideoVM
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_movie.*
import javax.inject.Inject

/**
 * A simple [DaggerFragment] subclass.
 */
class MovieFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var videoVM: VideoVM
    private lateinit var videoAdapter: VideoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoVM = activity?.getViewModel(viewModelFactory)
            ?: throw Throwable("Video viewmodel not init")

        context?.checkPermissions(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
            ),
            onGranted = { videoVM.videoFiles(context!!) }
        )

        videoAdapter = VideoAdapter {
            VideoPlayerFragment.newInstance(it)
                .show(childFragmentManager, VideoPlayerFragment.TAG)
        }

        videoVM.videos.observe(this@MovieFragment, Observer {
            it ?: return@Observer
            videoAdapter.swap(it)

            empty_placeholder.visibility = if (it.isEmpty())
                View.VISIBLE
            else
                View.GONE
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_videos.apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

}
