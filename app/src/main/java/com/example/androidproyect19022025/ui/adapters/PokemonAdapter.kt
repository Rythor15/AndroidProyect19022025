package com.example.androidproyect19022025.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproyect19022025.R
import com.example.androidproyect19022025.data.Pokemon
import com.example.androidproyect19022025.databinding.LayoutPokemonBinding
import com.squareup.picasso.Picasso

class PokemonAdapter(
    var pokeInfo: MutableList<Pokemon>
):  RecyclerView.Adapter<ViewHolderPokemon>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPokemon {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.layout_pokemon, parent, false)
        return ViewHolderPokemon(v)
    }

    override fun onBindViewHolder(holder: ViewHolderPokemon, position: Int) {
        val pokemonInfo = pokeInfo[position]
        holder.render(pokemonInfo)
    }

    override fun getItemCount() = pokeInfo.size

}

class ViewHolderPokemon(v: View): RecyclerView.ViewHolder(v){
    val binding = LayoutPokemonBinding.bind(v)

    fun render(pokemonInfo: Pokemon){
        binding.tvNombre.text=pokemonInfo.nombre
        Picasso.get().load(pokemonInfo.pokemonSprite.frontDefault).into(binding.imageView)
        binding.tvTipo.text = pokemonInfo.tipos[0].tipo.nombreTipo
        binding.tvTipo2.text = if (pokemonInfo.tipos.size > 1) pokemonInfo.tipos[1].tipo.nombreTipo else pokemonInfo.tipos[0].tipo.nombreTipo
        binding.tvHabilidad.text = pokemonInfo.habilidades[0].habilidad.nombreHabilidad
        binding.tvHabilidad2.text = if (pokemonInfo.habilidades.size > 1) pokemonInfo.habilidades[1].habilidad.nombreHabilidad else ""
        binding.tvHabilidad3.text = if (pokemonInfo.habilidades.size > 2) pokemonInfo.habilidades[2].habilidad.nombreHabilidad else ""
    }
}