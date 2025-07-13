package com.example.myapplication.Linguin

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.myapplication.AIpetApp.PetWorkManager
import com.example.myapplication.R
import java.util.concurrent.TimeUnit
import com.google.android.material.bottomnavigation.BottomNavigationView

class LinguinMainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var isMusicStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.linguinmainlayout)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomview)
        val toolbar = findViewById<Toolbar>(R.id.myToolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNavView.setupWithNavController(navController)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            val currentDestination = navController.currentDestination?.id
            if (currentDestination != R.id.liguinMainFragment) {
                navController.popBackStack(R.id.liguinMainFragment, false)
            }
        }

        requestMicrophonePermission()
        startPetBehaviorUpdates()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.linguinChat,
                R.id.linguinSettingsPanel,
                R.id.linguinShoppingPanel -> {
                    bottomNavView.visibility = View.GONE
                    toolbar.visibility = View.VISIBLE
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }
                else -> {
                    bottomNavView.visibility = View.VISIBLE
                    toolbar.visibility = View.GONE
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
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

    private fun requestMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                101
            )
        }
    }

    private fun startPetBehaviorUpdates() {
        val workRequest = PeriodicWorkRequestBuilder<PetWorkManager>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "PetUpdateWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
