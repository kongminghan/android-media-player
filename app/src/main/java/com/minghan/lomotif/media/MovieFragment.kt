package com.minghan.lomotif.media


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.minghan.lomotif.media.adapter.VideoAdapter
import com.minghan.lomotif.media.dagger.ViewModelFactory
import com.minghan.lomotif.media.extension.getViewModel
import com.minghan.lomotif.media.viewmodel.VideoVM
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.android.synthetic.main.fragment_movie.view.*
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

        videoAdapter = VideoAdapter {
            VideoPlayerFragment.newInstance(it)
                .show(childFragmentManager, VideoPlayerFragment.TAG)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_videos.apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        videoVM.videos.observe(this@MovieFragment, Observer {
            it ?: return@Observer
            videoAdapter.swap(it)

            empty_placeholder.visibility = if (it.isEmpty())
                View.VISIBLE
            else
                View.GONE

            view?.progress?.visibility = View.GONE
        })
    }

}
