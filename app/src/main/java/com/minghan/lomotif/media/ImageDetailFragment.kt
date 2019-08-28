package com.minghan.lomotif.media


import android.Manifest
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.*
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequest
import com.minghan.lomotif.media.dagger.ViewModelFactory
import com.minghan.lomotif.media.extension.checkPermissions
import com.minghan.lomotif.media.extension.getViewModel
import com.minghan.lomotif.media.extension.toast
import com.minghan.lomotif.media.viewmodel.ImageVM
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_image_detail.*
import javax.inject.Inject
import com.minghan.lomotif.media.extension.statusBarHeight
import kotlinx.android.synthetic.main.fragment_image_detail.view.*
import qiu.niorgai.StatusBarCompat


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

        StatusBarCompat.translucentStatusBar(activity!!, true)
        StatusBarCompat.changeToLightStatusBar(activity!!)

        with(view.toolbar) {
            setPadding(0, view.context.statusBarHeight, 0, 0)
            layoutParams.height = view.context.statusBarHeight + toolbar.layoutParams.height
        }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        download.setOnClickListener {
            val url = imageVM?.selectedImage?.value?.largeImageURL
                ?: return@setOnClickListener

            context?.checkPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                onGranted = {
                    (activity as? MainActivity)?.download(url)
                    context?.toast(R.string.downloading)
                },
                onDenied = {
                    context?.toast(R.string.no_permission_download)
                }
            )
        }
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

    override fun onDestroy() {
        super.onDestroy()
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        StatusBarCompat.changeToLightStatusBar(activity!!)

        val mContentView = activity!!.window.findViewById(Window.ID_ANDROID_CONTENT) as ViewGroup
        val mChildView = mContentView.getChildAt(0)
        if (mChildView != null) {
            mChildView.fitsSystemWindows = false
            ViewCompat.requestApplyInsets(mChildView)
        }
    }

}
