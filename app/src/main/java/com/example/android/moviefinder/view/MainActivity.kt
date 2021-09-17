package com.example.android.moviefinder.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.MainActivityBinding
import com.example.android.moviefinder.model.*
import com.example.android.moviefinder.view.favorites.FavoritesFragment
import com.example.android.moviefinder.view.home.HomeFragment
import com.example.android.moviefinder.view.ratings.RatingsFragment
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        fun getDefaultLocaleString(): String {
            Locale.getDefault().apply {
                return "${language}-${country}"
            }
        }
    }

    private val binding: MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }

    private val localMainReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.getStringExtra(NETWORK_STATUS)) {
                AVAILABLE_STATUS -> {
                    context?.let { onNetworkRestored(it) }
                }
                LOST_STATUS -> {
                    context?.let { onNetworkLost(it) }
                }
            }
        }
    }

    private fun onNetworkRestored(context: Context) {
        Thread {
            runOnUiThread {
                binding.connectivityBar.apply {
                    connectivityContainer.background =
                        AppCompatResources.getDrawable(context, R.drawable.green_bg)
                    connectivityTextView.text = getString(R.string.internet_restored)
                }
            }
            Thread.sleep(2000)
            runOnUiThread {
                binding.connectivityBar.connectivityContainer.hide()
            }
        }.start()
    }


    private fun onNetworkLost(context: Context) {
        binding.connectivityBar.apply {
            connectivityContainer.show()
            connectivityContainer.background =
                AppCompatResources.getDrawable(context, R.drawable.red_bg)
            connectivityTextView.text = getString(R.string.no_internet)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        initBottomNavigation()

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment.newInstance())
        }

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(localMainReceiver, IntentFilter(NETWORK_STATUS_INTENT_FILTER))
        NetworkMonitor(application).startNetworkCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.search -> Toast.makeText(this, "Searching", Toast.LENGTH_LONG).show()
        }
        return true
    }

    private fun initBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment.newInstance())
                R.id.favorites -> replaceFragment(FavoritesFragment.newInstance())
                R.id.ratings -> replaceFragment(RatingsFragment.newInstance())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.apply {
            for (i in 0 until backStackEntryCount) popBackStack()
            beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }
}