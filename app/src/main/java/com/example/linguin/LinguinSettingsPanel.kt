package com.example.myapplication.Linguin

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import com.example.myapplication.R


class LinguinSettingsPanel : Fragment() {

    private lateinit var musicSwitch: Switch
    private var mediaPlayer: MediaPlayer? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_linguin_settings_panel , container , false)
        musicSwitch = view.findViewById(R.id.musicswitch)
        return view
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        musicSwitch.setOnCheckedChangeListener { _, isChecked ->
            val activity = activity as? LinguinMainActivity
            if (isChecked) {
                activity?.startMusic()
            } else {
                activity?.stopMusic()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        musicSwitch.setOnCheckedChangeListener(null)
    }

}