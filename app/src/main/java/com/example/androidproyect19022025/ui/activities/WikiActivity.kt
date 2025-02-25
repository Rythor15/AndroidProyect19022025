package com.example.androidproyect19022025.ui.activities

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidproyect19022025.R
import com.example.androidproyect19022025.databinding.ActivityWikiBinding
import java.util.Locale

class WikiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWikiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWikiBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_wiki)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        inicializarWebView()
        setListeners()
    }
    private fun setListeners() {
        binding.swipe.setOnRefreshListener {
            binding.webView.reload()
        }
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val busqueda = p0.toString().trim().lowercase(Locale.ROOT)
                if (android.util.Patterns.WEB_URL.matcher(busqueda).matches()) {
                    binding.webView.loadUrl(busqueda)
                    return true
                }
                val url = "https://www.google.es/search?q=${busqueda}"
                binding.webView.loadUrl(url)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val color = parent?.getItemAtPosition(position)
                if (position != 0) {
                    findViewById<ConstraintLayout>(R.id.main_wiki).setBackgroundColor(Color.parseColor(color.toString()))
                } else {
                    findViewById<ConstraintLayout>(R.id.main_wiki).setBackgroundResource(R.drawable.fondoaplicacion)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun inicializarWebView() {
        binding.webView.webViewClient=object: WebViewClient(){

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.swipe.isRefreshing=true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.swipe.isRefreshing=false
            }
        }
        binding.webView.webChromeClient = object: WebChromeClient(){

        }
        //Activamos JavaScript
        binding.webView.settings.javaScriptEnabled=true

        binding.webView.loadUrl("https://www.wikidex.net/wiki/WikiDex")
    }

    override fun onBackPressed() {
        if(binding.webView.canGoBack()){
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}