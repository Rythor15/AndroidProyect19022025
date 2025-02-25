package com.example.androidproyect19022025.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproyect19022025.R
import com.example.androidproyect19022025.data.models.AmigoModel
import com.example.androidproyect19022025.databinding.AmigoLayoutBinding
import com.squareup.picasso.Picasso

class AmigoAdapter(
    var lista: MutableList<AmigoModel>,
    private var borrarContacto: (Int) -> Unit,
    private val updateContacto: (AmigoModel) -> Unit,
) : RecyclerView.Adapter<AmigoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmigoViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.amigo_layout, parent, false)
        return AmigoViewHolder(v)
    }

    override fun getItemCount() = lista.size


    override fun onBindViewHolder(holder: AmigoViewHolder, position: Int) {
        holder.render(lista[position], borrarContacto, updateContacto)
    }
}
class AmigoViewHolder (v: View) : RecyclerView.ViewHolder(v) {
    val binding = AmigoLayoutBinding.bind(v)
    fun render(
        a: AmigoModel,
        borrarContacto: (Int) -> Unit,
        updateContacto: (AmigoModel) -> Unit
    ) {
        binding.tvNombre.text = a.nombre
        binding.tvNombreJuego.text = a.apellido
        binding.tvEdad.text = a.edad.toString()
        Picasso.get().load(a.imagen).into(binding.imageView)

        binding.btnBorrar.setOnClickListener {
            borrarContacto(adapterPosition)
        }
        binding.btnUpdate.setOnClickListener {
            updateContacto(a)
        }
    }

}