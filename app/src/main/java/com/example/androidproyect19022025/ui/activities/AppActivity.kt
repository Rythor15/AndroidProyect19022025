package com.example.androidproyect19022025.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidproyect19022025.R
import com.example.androidproyect19022025.databinding.ActivityAppBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class AppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppBinding
    private var nombre = ""
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.ivPokemon.setImageResource(R.drawable.pokemon)
        auth = Firebase.auth
        recogerDatos()
        setListeners()
        title = "Menu"
    }
    private fun setListeners() {
        binding.btnSalir.setOnClickListener {
            finishAffinity()
        }
        binding.btnCerrar.setOnClickListener {
            auth.signOut()
            finish()
        }
        binding.btnCompeticiones.setOnClickListener {
            startActivity(Intent(this, MapaActivity::class.java))
        }
        binding.btnPokedex.setOnClickListener {
            startActivity(Intent(this, PokedexActivity::class.java))
        }
        binding.btnAmigos.setOnClickListener {
            startActivity(Intent(this, ListaAmigosActivity::class.java))
        }
        binding.btnWikipedia.setOnClickListener {
            startActivity(Intent(this, WikiActivity::class.java))
        }
        binding.btnVideos.setOnClickListener {
            startActivity(Intent(this, TutorialActivity::class.java))
        }
        binding.btnEquipos.setOnClickListener {
            startActivity(Intent(this, ListaEquipoPokemonActivity::class.java))
        }
    }

    private fun recogerDatos() {
        val datos = intent.extras
        nombre = datos?.getString("NOMBRE").toString()
        binding.tvNombre.text = (String.format("Bienvenido %s", nombre))
    }
}