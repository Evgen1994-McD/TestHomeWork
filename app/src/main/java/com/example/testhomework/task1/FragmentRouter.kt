package com.example.testhomework.task1

import androidx.fragment.app.FragmentManager

class FragmentRouter(
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) {
    private val totalScreens = 3
    private var currentScreen = 1

    fun navigateToNext() {
        if (currentScreen < totalScreens) {
            currentScreen++
            navigateToScreen(currentScreen)
        }
    }

    fun navigateToPrevious() {
        if (currentScreen > 1) {
            currentScreen--
            navigateToScreen(currentScreen)
        }
    }

    fun navigateToScreen(screenNumber: Int) {
        if (screenNumber in 1..totalScreens) {
            currentScreen = screenNumber
            val fragment = when (screenNumber) {
                1 -> Screen1Fragment()
                2 -> Screen2Fragment()
                3 -> Screen3Fragment()
                else -> return
            }

            val transaction = fragmentManager.beginTransaction()
                .replace(containerId, fragment)
            
            // Не добавляем первый экран в back stack
            if (screenNumber > 1) {
                transaction.addToBackStack(null)
            }
            
            transaction.commit()
        }
    }

    fun getCurrentScreen(): Int = currentScreen
}

