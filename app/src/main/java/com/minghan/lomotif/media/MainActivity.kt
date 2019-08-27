package com.minghan.lomotif.media

import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.minghan.lomotif.media.extension.setupWithNavController
import com.minghan.lomotif.media.extension.statusBarHeight
import com.minghan.lomotif.media.extension.toast
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2core.DownloadBlock
import com.tonyodev.fetch2core.Func
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.TRANSPARENT
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        with(toolbar) {
            setPadding(0, statusBarHeight, 0, 0)
            layoutParams.height = statusBarHeight + toolbar.layoutParams.height
        }

        // setup navigation
        val navControllerLiveData = bottom_nav.setupWithNavController(
            navGraphIds = listOf(
                R.navigation.nav_music,
                R.navigation.nav_video,
                R.navigation.nav_image
            ),
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = intent
        )

        navControllerLiveData.observe(this, Observer {
            it?.addOnDestinationChangedListener { _, destination, _ ->
                // update toolbar title
                toolbar.title = destination.label

                if (destination.id == R.id.imageDetailFragment) {
                    toolbar.visibility = View.GONE
                    bottom_nav.visibility = View.GONE
                } else {
                    toolbar.visibility = View.VISIBLE
                    bottom_nav.visibility = View.VISIBLE
                }
            }
        })
    }

    fun download(url: String) {
        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val fetchConfiguration = FetchConfiguration.Builder(this)
            .setDownloadConcurrentLimit(3)
            .build()

        val fileName = url.substring(url.lastIndexOf('/') + 1)

        val fetch = Fetch.getInstance(fetchConfiguration)

        val request = Request(url, "${downloadDir.absolutePath}/$fileName")

        fetch.enqueue(request, Func {
        }, Func {
            it.throwable?.printStackTrace()
        }).addListener(fetchListener)
    }

    private val fetchListener = object : FetchListener {
        override fun onAdded(download: Download) {
        }

        override fun onCancelled(download: Download) {
        }

        override fun onCompleted(download: Download) {
            toast(R.string.downloaded)
        }

        override fun onDeleted(download: Download) {
        }

        override fun onDownloadBlockUpdated(
            download: Download,
            downloadBlock: DownloadBlock,
            totalBlocks: Int
        ) {
        }

        override fun onError(download: Download, error: Error, throwable: Throwable?) {
            throwable?.printStackTrace()
            error.httpResponse?.toString()

            toast(R.string.download_failed)
        }

        override fun onPaused(download: Download) {
        }

        override fun onProgress(
            download: Download,
            etaInMilliSeconds: Long,
            downloadedBytesPerSecond: Long
        ) {
        }

        override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
        }

        override fun onRemoved(download: Download) {
        }

        override fun onResumed(download: Download) {
        }

        override fun onStarted(
            download: Download,
            downloadBlocks: List<DownloadBlock>,
            totalBlocks: Int
        ) {
        }

        override fun onWaitingNetwork(download: Download) {
            toast(R.string.no_network)
        }

    }

}
