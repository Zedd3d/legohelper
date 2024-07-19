package com.zeddikus.legohelper.base

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.zeddikus.legohelper.R
import com.zeddikus.legohelper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
        showBottomNav()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment,
                R.id.aboutFragment-> showBottomNav()

                else -> hideBottomNav()
            }
        }
    }

    private fun hideBottomNav() {
        binding.bottomNavigation.visibility = View.GONE
    }

    private fun showBottomNav() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }
}
