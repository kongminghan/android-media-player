package com.minghan.lomotif.media


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.transition.TransitionInflater
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.request.ImageRequest
import com.minghan.lomotif.media.dagger.ViewModelFactory
import com.minghan.lomotif.media.extension.getViewModel
import com.minghan.lomotif.media.extension.statusBarHeight
import com.minghan.lomotif.media.viewmodel.ImageVM
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_image_detail.*
import kotlinx.android.synthetic.main.fragment_image_detail.view.*
import javax.inject.Inject
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.provider.SyncStateContract.Helpers.update
import android.graphics.drawable.Animatable
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.image.ImageInfo


/**
 * A simple [DaggerFragment] subclass.
 */
class ImageDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var imageVM: ImageVM? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_image_detail, container, false)

        val params = ViewGroup.MarginLayoutParams(view.btn_back.layoutParams)
        params.setMargins(
            0,
            resources.getDimensionPixelSize(R.dimen.nav_icon_margin) + (view.context?.statusBarHeight ?: 0),
            0,
            0
        )

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedElementTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = sharedElementTransition
        sharedElementReturnTransition = sharedElementTransition

        imageVM = activity?.getViewModel(viewModelFactory)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        imageVM?.selectedImage?.observe(this, Observer {
            it?.largeImageURL ?: return@Observer

            val controller = Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(ImageRequest.fromUri(it.previewURL))
                .setImageRequest(ImageRequest.fromUri(it.largeImageURL))
                .setOldController(image.controller)

            controller.controllerListener = object : BaseControllerListener<ImageInfo>() {
                override fun onFinalImageSet(
                    id: String?,
                    imageInfo: ImageInfo?,
                    animatable: Animatable?
                ) {
                    super.onFinalImageSet(id, imageInfo, animatable)
                    if (imageInfo == null || image == null) {
                        return
                    }
                    image.update(imageInfo.width, imageInfo.height)
                }
            }

            image.controller = controller.build()
            name.text = it.user
            avatar.setImageURI(it.userImageURL)
            views.text = getString(R.string.num_views, it.views ?: 0)
            favorites.text = it.favorites?.toString() ?: "0"
            likes.text = it.likes?.toString() ?: "0"
        })
    }

}
