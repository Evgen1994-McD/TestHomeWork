package com.example.testhomework

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.testhomework.databinding.ActivityMainBinding
import com.example.testhomework.task1.FragmentRouter
import com.example.testhomework.task1.NavigationListener
import com.example.testhomework.task1.Screen1Fragment
import com.example.testhomework.task1.Screen2Fragment
import com.example.testhomework.task1.Screen3Fragment
import com.example.testhomework.task2.ChargingNotificationWorker

class MainActivity : AppCompatActivity(), NavigationListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var router: FragmentRouter

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

        router = FragmentRouter(supportFragmentManager, binding.fragmentContainer.id)

        if (savedInstanceState == null) {
            router.navigateToScreen(1)
        }

        // Устанавливаем NavigationListener для текущего фрагмента после транзакции
        binding.fragmentContainer.post {
            updateFragmentNavigationListener()
        }

        // Создаем канал уведомлений и планируем WorkManager задачу
        createNotificationChannel()
        scheduleChargingNotificationWork()
    }

    override fun onResume() {
        super.onResume()
        updateFragmentNavigationListener()
    }

    private fun updateFragmentNavigationListener() {
        val currentFragment = supportFragmentManager.findFragmentById(binding.fragmentContainer.id)
        when (currentFragment) {
            is Screen1Fragment -> currentFragment.setNavigationListener(this)
            is Screen2Fragment -> currentFragment.setNavigationListener(this)
            is Screen3Fragment -> currentFragment.setNavigationListener(this)
        }
    }

    override fun onNextClicked() {
        router.navigateToNext()
        // Обновляем listener после транзакции
        supportFragmentManager.executePendingTransactions()
        updateFragmentNavigationListener()
    }

    override fun onPreviousClicked() {
        router.navigateToPrevious()
        // Обновляем listener после транзакции
        supportFragmentManager.executePendingTransactions()
        updateFragmentNavigationListener()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Уведомления о зарядке",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Уведомления о статусе зарядки устройства"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleChargingNotificationWork() {
        val workRequest = OneTimeWorkRequestBuilder<ChargingNotificationWorker>()
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }

    companion object {
        private const val CHANNEL_ID = "charging_notification_channel"
    }
}

