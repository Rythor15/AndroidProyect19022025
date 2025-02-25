package com.example.androidproyect19022025.ui.activities

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidproyect19022025.R
import com.example.androidproyect19022025.databinding.ActivityTutorialBinding

class TutorialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTutorialBinding
    private lateinit var mediaController: MediaController
    var resourceVideo = R.raw.videopokemon
    var rutaVideo = "android.resource://com.example.androidproyect19022025/$resourceVideo"
    var posicion = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mediaController = MediaController(this)
        if (savedInstanceState != null) {
            posicion = savedInstanceState.getInt("POSICION")
            rutaVideo = savedInstanceState.getString("RUTA", "")
            reproducirVideo()
        }
    }

    private fun reproducirVideo() {
        val uri = Uri.parse(rutaVideo)
        try {
            binding.videoView.setVideoURI(uri)
            binding.videoView.requestFocus()
            binding.videoView.start()
        } catch (e: Exception) {
            Log.d("Error:  >>> ", e.message.toString())
        }
        binding.videoView.setMediaController(mediaController)
        mediaController.setAnchorView(binding.videoView)
    }
    // Esta funcion recoje los datos del video para cuando cambiemos la posicion de la pantalla podamos seguir por donde ibamos

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("POSICION", binding.videoView.currentPosition)
        outState.putString("RUTA", rutaVideo)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        rutaVideo = savedInstanceState.getString("RUTA", "")
        posicion = savedInstanceState.getInt("POSICION")
    }

    override fun onPause() {
        super.onPause()
        if (binding.videoView.isPlaying) {
            posicion = binding.videoView.currentPosition
            binding.videoView.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (rutaVideo.isNotEmpty()) {
            reproducirVideo()
        }
    }
}