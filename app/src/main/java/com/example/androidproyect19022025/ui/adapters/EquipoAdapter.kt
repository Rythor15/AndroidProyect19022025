package com.example.androidproyect19022025.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproyect19022025.R
import com.example.androidproyect19022025.data.models.EquipoPokemon
import com.example.androidproyect19022025.databinding.LayoutEquipoBinding

class EquipoAdapter(
    var lista: MutableList<EquipoPokemon>,
    private var onBorrar: (EquipoPokemon) -> Unit,
    private var onEdit: (EquipoPokemon) -> Unit
): RecyclerView.Adapter<EquipoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_equipo, parent, false)
        return EquipoViewHolder(v)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        holder.render(lista[position], onBorrar, onEdit)
    }
}

class EquipoViewHolder (v: View): RecyclerView.ViewHolder(v) {
    val binding = LayoutEquipoBinding.bind(v)
    fun render(item: EquipoPokemon, onBorrar: (EquipoPokemon) -> Unit, onEdit: (EquipoPokemon) -> Unit) {
        binding.tvEntrenador.text = item.entrenador
        binding.tvPokemon1.text = item.pokemon1
        binding.tvPokemon2.text = item.pokemon2
        binding.tvPokemon3.text = item.pokemon3
        binding.tvPokemon4.text = item.pokemon4
        binding.tvPokemon5.text = item.pokemon5
        binding.tvPokemon6.text = item.pokemon6

        binding.btnBorrar.setOnClickListener {
            onBorrar(item)
        }
        binding.btnEditar.setOnClickListener {
            onEdit(item)
        }
    }
}
