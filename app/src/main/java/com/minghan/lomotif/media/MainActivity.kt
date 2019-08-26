package com.minghan.lomotif.media

import android.os.Bundle
import androidx.lifecycle.Observer
import com.minghan.lomotif.media.extension.setupWithNavController
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            }
        })
    }

}
