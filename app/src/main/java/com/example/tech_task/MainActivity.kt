package com.example.tech_task

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tech_task.api.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnNavView = findViewById<BottomNavigationView>(R.id.bottomNav)
        val controller = findNavController(R.id.fragmentContainerView)
        btnNavView.setupWithNavController(controller)

        fetchCharacters(page = 1)

    }

    fun fetchCharacters(name: String? = null, page: Int? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.getCharacters(name, page)
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        Log.d("Retrofit", "Fetched characters: ${it.results}")
                        it.results.forEach { character ->
                            Log.d("Retrofit", "ID: ${character.id}, Name: ${character.name}, Status: ${character.status}")
                        }
                    }
                } else {
                    Log.e("Retrofit", "Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Retrofit", "Exception: ${e.localizedMessage}")
            }
        }
    }
}