package com.example.androidproyect19022025.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidproyect19022025.R
import com.example.androidproyect19022025.data.models.EquipoPokemon
import com.example.androidproyect19022025.databinding.ActivityAddEquipoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddEquipoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEquipoBinding

    private var entrenador = ""
    private var pokemon1 = ""
    private var pokemon2 = ""
    private var pokemon3 = ""
    private var pokemon4 = ""
    private var pokemon5 = ""
    private var pokemon6 = ""
    private var editando = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEquipoBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.tvTitulo.text = "Nuevo Equipo"
        setListeners()
        recogerDatos()
    }
    //--------------------------------------------------------------------------------------------------
    private fun setListeners() {
        binding.btnCancelar.setOnClickListener{
            finish()
        }
        binding.btnAdd.setOnClickListener {
            addItem()
        }
    }
    //--------------------------------------------------------------------------------------------------
    private fun addItem() {
        if (!asignarDatos()) return
        //Datos correctos
        val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("EquipoPokemon")
        val item = EquipoPokemon(entrenador, pokemon1, pokemon2, pokemon3, pokemon4, pokemon5, pokemon6)
        val nodo = entrenador
        database.child(nodo).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && !editando) {
                    Toast.makeText(this@AddEquipoActivity, "El entrenador ya esta registrado", Toast.LENGTH_SHORT).show()
                } else {
                    database.child(nodo).setValue(item).addOnSuccessListener {
                        finish()
                    }
                        .addOnFailureListener {
                            Toast.makeText(this@AddEquipoActivity, "Error al guardar", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    //--------------------------------------------------------------------------------------------------
    private fun asignarDatos(): Boolean {
        entrenador = binding.etEntrenador.text.toString().trim()
        if (entrenador.length<3) {
            binding.etEntrenador.error = "Error, el nombre del entrenador debe tener más de 3 caracteres."
            return false
        }
        pokemon1 = binding.etPokemon1.text.toString().trim()
        pokemon2 = binding.etPokemon2.text.toString().trim()
        pokemon3 = binding.etPokemon3.text.toString().trim()
        pokemon4 = binding.etPokemon4.text.toString().trim()
        pokemon5 = binding.etPokemon5.text.toString().trim()
        pokemon6 = binding.etPokemon6.text.toString().trim()

        return true
    }


    private fun recogerDatos(){

        var bundle = intent.extras

        if (bundle != null){
            val item = bundle.getSerializable("ITEM") as EquipoPokemon
            editando = true
            binding.tvTitulo.text = "Editar Equipo"
            binding.etEntrenador.isEnabled = false
            binding.etEntrenador.setText(item.entrenador)
            binding.etPokemon1.setText(item.pokemon1)
            binding.etPokemon2.setText(item.pokemon2)
            binding.etPokemon3.setText(item.pokemon3)
            binding.etPokemon4.setText(item.pokemon4)
            binding.etPokemon5.setText(item.pokemon5)
            binding.etPokemon6.setText(item.pokemon6)
        }
    }
}