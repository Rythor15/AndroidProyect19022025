package com.example.androidproyect19022025.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.example.androidproyect19022025.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

//IMPLEMENTAR FUNCIONALIDAD DE SENSORES

class MapaActivity : AppCompatActivity(), OnMapReadyCallback {

    private val LOCATION_CODE = 10000
    private val locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permisos ->
        if (permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true
            ||
            permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            gestionarLocalizacion()
        } else {
            Toast.makeText(this, "El usuario denegó los permisos...", Toast.LENGTH_SHORT).show()
        }

    }

    private lateinit var map: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mapa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        iniciarFramentMapa()
        title = "Mapa de torneos"
    }

    private fun iniciarFramentMapa() {
        val fragment = SupportMapFragment()
        fragment.getMapAsync(this)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fm_maps, fragment)
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        map.uiSettings.isZoomControlsEnabled=true
        gestionarLocalizacion()
        ponerMarcador(LatLng(33.82475463077455, -117.90661939520065), "Mundial Pokemon 2025")
        ponerMarcador(LatLng(52.44851528365187, -1.7185531875234843), "Regional Pokemon Europa Reino Unido")
        ponerMarcador(LatLng(59.277428636742854, 18.01492628216522), "Regional Pokemon Europa Suecia")
        mostrarLugarTorneo(LatLng(33.82475463077455, -117.90661939520065), 10f)
    }

    private fun ponerMarcador(coordenadas: LatLng, titulo: String) {
        val marker = MarkerOptions().position(coordenadas).title(titulo)
        map.addMarker(marker)
    }

    private fun mostrarLugarTorneo(coordenadas: LatLng, zoom: Float) {
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordenadas,zoom),
            3500,
            null
        )
    }

    private fun gestionarLocalizacion() {
        if (!::map.isInitialized) return
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
        } else {
            pedirPermisos()
        }
    }

    private fun pedirPermisos() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
            ||
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            mostrarExplicacion()
        } else {
            escogerPermisos()
        }
    }

    private fun escogerPermisos() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun mostrarExplicacion() {
        AlertDialog.Builder(this)
            .setTitle("Permisos de Ubicacion")
            .setMessage("Para el uso adecuado de esta increible aplicacion necesitamos permisos de ubicacion ")
            .setNegativeButton("Cancelar") {
                    dialog, _ -> dialog.dismiss()
            }
            .setCancelable(false)
            .setPositiveButton("Aceptar") {
                    dialog , _ -> startActivity(Intent(Settings.ACTION_APPLICATION_SETTINGS))
                dialog.dismiss()
            }
            .create()
            .dismiss()

    }

    override fun onRestart() {
        super.onRestart()
        gestionarLocalizacion()
    }
}