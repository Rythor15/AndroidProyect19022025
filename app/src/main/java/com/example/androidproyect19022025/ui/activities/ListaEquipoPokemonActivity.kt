package com.example.androidproyect19022025.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidproyect19022025.R
import com.example.androidproyect19022025.data.models.EquipoPokemon
import com.example.androidproyect19022025.data.providers.PokemonProvider
import com.example.androidproyect19022025.databinding.ActivityListaAmigosBinding
import com.example.androidproyect19022025.databinding.ActivityListaEquipoPokemonBinding
import com.example.androidproyect19022025.ui.adapters.EquipoAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class ListaEquipoPokemonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaEquipoPokemonBinding
    val adapter = EquipoAdapter(mutableListOf<EquipoPokemon>(), {item -> borrarItem(item)}, {item -> editarItem(item)})
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaEquipoPokemonBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().getReference("EquipoPokemon")
        setRecycler()
        setListeners()
    }

    private fun setRecycler() {
        val layautManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layautManager
        binding.recyclerView.adapter = adapter
        recuperarDatosAgenda()
    }

    private fun recuperarDatosAgenda() {
        val equipoProvider = PokemonProvider()
        equipoProvider.getDatos { todosLosRegistros ->
            binding.ivEquipos.visibility = if(todosLosRegistros.isEmpty()) View.VISIBLE else View.INVISIBLE
            adapter.lista = todosLosRegistros
            adapter.notifyDataSetChanged()
        }
    }

    private fun setListeners() {
        binding.fabAdd.setOnClickListener {
            irActivityAdd()
        }
    }

    private fun irActivityAdd(bundle: Bundle ?= null) {
        val i = Intent(this, AddEquipoActivity::class.java)
        if (bundle != null) {
            i.putExtras(bundle)
        }
        startActivity(i)
    }

    private fun borrarItem(item: EquipoPokemon) {
        database.child(item.entrenador).removeValue()
            .addOnSuccessListener {
                val position = adapter.lista.indexOf(item)
                if (position != 1) {
                    adapter.lista.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    Toast.makeText(this, "Equipo Borrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al borrar el equipo", Toast.LENGTH_SHORT).show()
            }
    }

    private fun editarItem(item: EquipoPokemon) {
        val bundle = Bundle().apply {
            putSerializable("ITEM", item)
        }
        irActivityAdd(bundle)
    }

    override fun onResume() {
        super.onResume()
        recuperarDatosAgenda()
    }
}