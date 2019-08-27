package com.minghan.lomotif.media


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.minghan.lomotif.media.adapter.ImageAdapter
import com.minghan.lomotif.media.constant.Constant.Companion.LOADING
import com.minghan.lomotif.media.dagger.ViewModelFactory
import com.minghan.lomotif.media.data.SpaceColumnItemDecoration
import com.minghan.lomotif.media.extension.getViewModel
import com.minghan.lomotif.media.extension.screenWidth
import com.minghan.lomotif.media.viewmodel.ImageVM
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_gallery.*
import javax.inject.Inject

/**
 * A simple [DaggerFragment] subclass.
 */
class GalleryFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var imageVM: ImageVM? = null
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageVM = activity?.getViewModel(viewModelFactory)

        imageAdapter = ImageAdapter { view, image ->
            imageVM?.selectedImage?.postValue(image)
            val extras = FragmentNavigatorExtras(
                view to "imageView"
            )
            findNavController().navigate(
                R.id.action_gallery_to_imageDetail,
                null,
                null,
                extras
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        imageVM?.images?.observe(this, Observer {
            imageAdapter.submitList(it)
        })

        imageVM?.networkState?.observe(this, Observer {
            if (it == LOADING) {
                progress.visibility = View.VISIBLE
            } else {
                progress.visibility = View.GONE
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var spanCount = 3

        context?.screenWidth?.let {
            spanCount = it / 300
        }

        rv_images.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(context, spanCount)
            addItemDecoration(
                SpaceColumnItemDecoration(
                    spanCount,
                    resources.getDimensionPixelSize(
                        R.dimen.image_item_space
                    )
                )
            )
        }
    }

}
