package com.example

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.core.databinding.ActivityMainBinding
import com.task1_2.FlowerDatabase

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.fragmentContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Инициализация базы данных
        // База автоматически создается при первом вызове getDatabase()
        // Если база пуста, она будет заполнена начальными данными
        val database = FlowerDatabase.getDatabase(applicationContext)
        val dao = database.flowerShopDao()
        
        // База данных готова к использованию
        // При первом запуске приложения данные будут автоматически заполнены
    }

}

