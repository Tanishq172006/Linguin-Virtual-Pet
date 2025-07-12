package com.example.myapplication.Linguin

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class LinguinMainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var isMusicStarted = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.linguinmainlayout)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomview)


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment


        val navController = navHostFragment.navController

        bottomNavView.setupWithNavController(navController)
        val toolbar = findViewById<Toolbar>(R.id.myToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        setSupportActionBar(toolbar)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.linguinChat,
                R.id.linguinSettingsPanel,
                R.id.linguinShoppingPanel -> {
                    bottomNavView.visibility = View.GONE
                    toolbar.visibility = View.VISIBLE
                    supportActionBar?.setDisplayHomeAsUpEnabled(true) // Show back arrow
                }
                else -> {
                    bottomNavView.visibility = View.VISIBLE
                    toolbar.visibility = View.GONE
                    supportActionBar?.setDisplayHomeAsUpEnabled(false) // Hide back arrow
                }
            }
        }



    }

    fun startMusic() {
        if (!isMusicStarted) {
            mediaPlayer = MediaPlayer.create(this, R.raw.megalovania).apply {
                isLooping = true
                start()
            }
            isMusicStarted = true
        } else if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    fun stopMusic() {
        mediaPlayer?.pause()
        mediaPlayer?.seekTo(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        isMusicStarted = false
    }
}
