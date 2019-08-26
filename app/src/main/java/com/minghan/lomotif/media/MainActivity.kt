package com.minghan.lomotif.media

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.minghan.lomotif.media.extension.setupWithNavController
import com.minghan.lomotif.media.extension.statusBarHeight
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

}
