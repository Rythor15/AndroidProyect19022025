package com.example.androidproyect19022025.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidproyect19022025.R
import com.example.androidproyect19022025.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val datos = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val cuenta = datos.getResult(ApiException::class.java)
                if (cuenta != null) {
                    val credenciales = GoogleAuthProvider.getCredential(cuenta.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credenciales)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                irActivityApp()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                }
            } catch (e: ApiException) {
                Log.d("ERROR DE API:>>>>", e.message.toString())
            }
        }
        if (it.resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "El usuario canceló", Toast.LENGTH_SHORT).show()
        }
    }

    private lateinit var auth: FirebaseAuth
    private var nombre = ""
    private var email = ""
    private var password = ""
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        setListeners()
        title = "Login"
    }
    private fun setListeners() {
        binding.btnSalir.setOnClickListener {
            finish()
        }
        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.btnRegistrar.setOnClickListener {
            registrar()
        }
        binding.btnGoogle.setOnClickListener {
            loginGoogle()
        }
    }

    private fun registrar() {
        if (!datosCorrectos()) return
        // Datos Correctos, procedemos a registrar al usuario
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    //Si el usuario se ha creado vamos a iniciar sesion con él.
                    login()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun datosCorrectos(): Boolean {
        email = binding.tfEmail.text.toString().trim()
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tfEmail.error  = "Se esperaba una direccion de email correcta."
            return false
        }
        password = binding.tfPassword.text.toString().trim()
        if (password.length < 6) {
            binding.tfPassword.error = "Error, la contraseña debe tener al menos 6 carácteres"
            return false
        }

        return true
    }

    private fun login() {
        if (!datosCorrectos()) return
        // Los datos ya esta validados
        // Vamos a logear al usuario
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // all (si pongo t0do se pone amarillo y no me gusta eso) ha ido bien
                    irActivityApp()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun loginGoogle() {
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // Se necesita usar ese token que es el que hemos creado y no el google_app_id
            .requestIdToken(getString(R.string.google_client_id))
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(this, googleConf)

        googleClient.signOut() // Fundamental para que no haga login automatico si he cerrado sesion
        // Basicamente sirve para que te permita registrarte con mas de una cuenta de google.

        responseLauncher.launch(googleClient.signInIntent)
    }

    private fun irActivityApp(){
        nombre = binding.tfNombre.text.toString().trim()
        val bundle = Bundle().apply {
            putString("NOMBRE", nombre)
        }

        val i = Intent(this, AppActivity::class.java)
        i.putExtras(bundle)
        startActivity(i)
    }

    override fun onStart() {
        super.onStart()
        val usuario = auth.currentUser
        if (usuario != null) irActivityApp()
    }
}