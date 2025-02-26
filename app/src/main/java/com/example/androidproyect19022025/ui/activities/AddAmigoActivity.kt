package com.example.androidproyect19022025.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidproyect19022025.R
import com.example.androidproyect19022025.data.models.AmigoModel
import com.example.androidproyect19022025.data.db.CrudAmigos
import com.example.androidproyect19022025.databinding.ActivityAddAmigoBinding

class AddAmigoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAmigoBinding
    private var nombre=""
    private var edad=0
    private var apellido=""
    private var id=1
    private var imagen=""
    private var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAmigoBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recogerContacto()
        setListeners()
        title = "Añadir Amigo"

        if(isUpdate){
            binding.etTitle2.text="Editar Amigo"
            binding.btn2Enviar.text="Editar"
        }else{
            binding.etTitle2.text="Crear Amigo"
        }
    }
    private fun setListeners() {
        binding.btnCancelar.setOnClickListener{
            finish()
        }
        binding.btn2Reset.setOnClickListener{
            limpiar()
        }
        binding.btn2Enviar.setOnClickListener{
            guardarRegistro()
        }
        binding.sbEdad.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                edad = p1
                Log.d("EDAD", edad.toString());

                binding.tvEdad.text = String.format(getString(R.string.tv_poner_edad), edad)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
    }

    private fun guardarRegistro() {
        if(datosCorrectos()){
            imagen = "https://dummyimage.com/200X200/000/fff.png&text=" + (nombre.substring(0,3)).uppercase()
            val contacto = AmigoModel(id, nombre, edad, apellido, imagen)
            if (!isUpdate) {
                if (CrudAmigos().create(contacto)!=-1L){
                    Toast.makeText(this, "Se ha añadio un registro a la agenda", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    binding.etApellido.error = "NickName duplicado!!!"
                }
            } else {
                if (CrudAmigos().actualizar(contacto)) {
                    Toast.makeText(this, "Registro Actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    binding.etApellido.error = "NickName duplicado!!!"
                }
            }
        }
    }

    private fun datosCorrectos(): Boolean {
        nombre = binding.etNombre.text.toString().trim()
        apellido = binding.etApellido.text.toString().trim()

        if (nombre.length<3) {
            binding.etNombre.error="El campo nombre debe tener mas de 3 carácteres"
            return false
        }
        if (apellido.length < 5) {
            binding.etApellido.error = "El campo apellido debe tener mas de 5 carácteres"
            return false
        }
        return true
    }

    private fun limpiar() {
        binding.etNombre.setText("")
        binding.etApellido.setText("")
        binding.sbEdad.progress=0
        binding.tvEdad.text = String.format(getString(R.string.tv_poner_edad), 0)
    }


    private fun recogerContacto() {
        val datos=intent.extras
        if (datos!=null){
            val a = datos.getSerializable("AMIGO") as AmigoModel
            isUpdate = true
            nombre = a.nombre
            edad = a.edad
            imagen = a.imagen
            apellido = a.apellido
            id = a.id
            pintarDatos()
        }
    }

    private fun pintarDatos() {
        binding.etNombre.setText(nombre)
        binding.etApellido.setText(apellido)
        binding.sbEdad.progress = edad
        binding.tvEdad.text = String.format(getString(R.string.tv_poner_edad), edad)
    }

}