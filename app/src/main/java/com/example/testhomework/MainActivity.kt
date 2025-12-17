package com.example.testhomework

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.testhomework.databinding.ActivityMainBinding
import com.example.testhomework.task1.FragmentRouter
import com.example.testhomework.task1.NavigationListener
import com.example.testhomework.task1.Screen1Fragment
import com.example.testhomework.task1.Screen2Fragment
import com.example.testhomework.task1.Screen3Fragment
import com.example.testhomework.task2.ChargingNotificationWorker
import com.example.testhomework.task3.ProgressRectangleFragment

class MainActivity : AppCompatActivity(), NavigationListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var router: FragmentRouter
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("MainActivity", "Увед 0к")
        } else {
            Log.w("MainActivity", "Увед не ок")
        }
    }

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

        binding.fragmentContainer.post {
            updateFragmentNavigationListener()
        }

        createNotificationChannel()
        requestNotificationPermission()
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
            is ProgressRectangleFragment -> currentFragment.setNavigationListener(this)
        }
    }

    override fun onNextClicked() {
        router.navigateToNext()
        supportFragmentManager.executePendingTransactions()
        updateFragmentNavigationListener()
    }

    override fun onPreviousClicked() {
        router.navigateToPrevious()
        supportFragmentManager.executePendingTransactions()
        updateFragmentNavigationListener()
    }

    private fun createNotificationChannel() {
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

    private fun requestNotificationPermission() {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }

    }
    
    private fun scheduleChargingNotificationWork() {
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()
        val workRequest = OneTimeWorkRequestBuilder<ChargingNotificationWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "charging_notification_work",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }

    companion object {
        private const val CHANNEL_ID = "charging_notification_channel"
    }
}

