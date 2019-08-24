package com.minghan.lomotif.media

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAppCompatActivity() {

    private val navController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup navigation
        bottom_nav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            // update toolbar title
            toolbar.title = destination.label
        }
    }
}
