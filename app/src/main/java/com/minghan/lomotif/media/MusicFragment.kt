package com.minghan.lomotif.media


import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.minghan.lomotif.media.adapter.MusicAdapter
import com.minghan.lomotif.media.dagger.ViewModelFactory
import com.minghan.lomotif.media.extension.checkPermissions
import com.minghan.lomotif.media.extension.getViewModel
import com.minghan.lomotif.media.viewmodel.MusicVM
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_music.*
import javax.inject.Inject

/**
 * A simple [DaggerFragment] subclass.
 */
class MusicFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var musicVM: MusicVM
    private lateinit var musicAdapter: MusicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        musicVM = activity?.getViewModel(viewModelFactory)
            ?: throw Throwable("Music viewmodel not init")

        musicAdapter = MusicAdapter {
            MusicPlayerFragment.newInstance(it)
                .show(childFragmentManager, MusicPlayerFragment.TAG)
        }

        musicVM.musics.observe(this@MusicFragment, Observer {
            it ?: return@Observer
            musicAdapter.swap(it)

            empty_placeholder.visibility = if (it.isEmpty())
                View.VISIBLE
            else
                View.GONE
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_musics.apply {
            adapter = musicAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}
