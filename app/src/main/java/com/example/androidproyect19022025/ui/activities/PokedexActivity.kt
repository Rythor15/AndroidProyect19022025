package com.example.androidproyect19022025.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidproyect19022025.R
import com.example.androidproyect19022025.data.Pokemon
import com.example.androidproyect19022025.data.api.ObjectApiClient.apiClient
import com.example.androidproyect19022025.databinding.ActivityPokedexBinding
import com.example.androidproyect19022025.ui.adapters.PokemonAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokedexActivity : AppCompatActivity() {

    lateinit var binding: ActivityPokedexBinding
    var pokeInfo = mutableListOf<Pokemon>()
    var adapter = PokemonAdapter(pokeInfo)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokedexBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setListener()
        setRecycler()
        title = "Pokedex"
    }

    private fun setListener() {
        binding.shiny.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Funcion por implementar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setRecycler() {
        val layoutManager = GridLayoutManager(this, 2)
        binding.rvPokemon.layoutManager = layoutManager
        binding.rvPokemon.adapter = adapter
        capturarPokemon()
    }

    private fun capturarPokemon() {
        val listaPokemonInfo = mutableListOf<Pokemon>()
        val numeroPokes = 1302

        lifecycleScope.launch {
            for (i in 1..numeroPokes) {
                try {
                    // Obtenemos la información del Pokémon en el hilo IO
                    val pokemonInfo = withContext(Dispatchers.IO) {
                        apiClient.getPokemonInfo(i)
                    }

                    if (pokemonInfo != null) {
                        listaPokemonInfo.add(pokemonInfo)

                        // Actualizamos la UI inmediatamente después de recibir la respuesta
                        withContext(Dispatchers.Main) {
                            adapter.pokeInfo = listaPokemonInfo
                            adapter.notifyDataSetChanged()
                        }
                    } else {
                        Log.e("PokemonError", "El Pokémon con ID $i no se pudo obtener")
                    }
                } catch (e: Exception) {
                    Log.e(
                        "PokemonError",
                        "Error al obtener el Pokémon con ID $i: ${e.message}"
                    )
                }
            }
        }
    }
}