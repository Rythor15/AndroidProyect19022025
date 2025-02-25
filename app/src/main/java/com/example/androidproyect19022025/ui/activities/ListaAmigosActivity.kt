package com.example.androidproyect19022025.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidproyect19022025.R
import com.example.androidproyect19022025.data.models.AmigoModel
import com.example.androidproyect19022025.data.db.CrudAmigos
import com.example.androidproyect19022025.databinding.ActivityListaAmigosBinding
import com.example.androidproyect19022025.ui.adapters.AmigoAdapter

class ListaAmigosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaAmigosBinding
    var lista = mutableListOf<AmigoModel>()
    private lateinit var adapter: AmigoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListaAmigosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setListeners()
        setRecycler()
        title = "Mis Amigos"
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lista_amigos, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menuItem_salir -> {
                finish()
            }
            R.id.menuItem_borrar -> {
                confirmarBorrado()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmarBorrado() {
        val builder = AlertDialog.Builder(this)
            .setTitle("¿Borrar Amigos?")
            .setMessage("¿Borrar todos los amigos de la lista?")
            .setNegativeButton("CANCELAR") {
                    dialog,_->dialog.dismiss()
            }// La doble barra baja significa que los parametros que recibe son opcionales
            .setPositiveButton("ACEPTAR") {
                    _,_-> CrudAmigos().borrarTodo()
                setRecycler()
            }
            .create()
            .show()

    }

    private fun traerRegistros() {
        lista = CrudAmigos().read()
        if (lista.size > 0) {
            binding.ivContactos.visibility = View.INVISIBLE
        } else {
            binding.ivContactos.visibility = View.VISIBLE

        }
    }

    private fun setRecycler() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        traerRegistros()
        adapter = AmigoAdapter(lista, {posicion->borrarContacto(posicion)}, {contacto -> update(contacto)})
        binding.recyclerView.adapter = adapter

    }

    private fun setListeners() {
        binding.fabAdd.setOnClickListener{
            startActivity(Intent(this, AddAmigoActivity::class.java))
        }
    }

    private fun update(a: AmigoModel) {
        val i = Intent(this, AddAmigoActivity::class.java).apply {
            putExtra("AMIGO", a)
        }
        startActivity(i)
    }

    fun borrarContacto(position:Int) {
        val id = lista[position].id
        //Lo elimino de la lista
        lista.removeAt(position)
        //Lo elimino de la BD
        if (CrudAmigos().borrar(id)){
            adapter.notifyItemRemoved(position)
            Toast.makeText(this, "Se eliminó correctamente", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No se elimino ningún registro", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRestart() {
        super.onRestart()
        setRecycler()
    }

}