package com.example.androidproyect19022025.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
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
import com.example.androidproyect19022025.databinding.ActivityMapaBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapaActivity : AppCompatActivity(), OnMapReadyCallback, SensorEventListener {

    private lateinit var binding: ActivityMapaBinding
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
    private lateinit var sensorManager: SensorManager
    private var giroscopio: Sensor? = null

    private lateinit var map: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapaBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        iniciarFramentMapa()
        title = "Mapa de torneos"
        sensorManager=getSystemService(SENSOR_SERVICE) as SensorManager
        iniciarSensor()

    }

    private fun mensajesMarcadores() {
        map.setOnMarkerClickListener {
            var value = it.title.toString()
            when (value) {
                "Mundial Pokemon 2025" -> {
                    Toast.makeText(this, String.format(getString(R.string.texto_toast) + " " + value), Toast.LENGTH_SHORT).show()
                    true
                }
                "Regional Pokemon Europa Reino Unido" -> {
                    Toast.makeText(this, String.format(getString(R.string.texto_toast) + value), Toast.LENGTH_SHORT).show()
                    true
                }
                "Regional Pokemon Europa Suecia" -> {
                    Toast.makeText(this, String.format(getString(R.string.texto_toast) + value), Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    false
                }

            }
        }
    }

    private fun iniciarSensor() {
        giroscopio=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
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
        mensajesMarcadores()
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

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        ponerListenerSensores(giroscopio)
    }

    private fun ponerListenerSensores(sensor: Sensor?){
        if (sensor!=null){
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.values?.isNotEmpty() == true) {
            when (event.sensor.type) {
                Sensor.TYPE_GYROSCOPE -> {
                    pintarValores(binding.tvEjeX, event.values[0])
                    pintarValores(binding.tvEjeY, event.values[1])
                    pintarValores(binding.tvEjeZ, event.values[2])
                }
            }
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
    private fun pintarValores(tv: TextView, valor: Float){
        val v=String.format("%.3f",valor)
        tv.text=v

    }

    override fun onRestart() {
        super.onRestart()
        gestionarLocalizacion()
    }
}