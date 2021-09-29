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
import com.example.android.moviefinder.view.contacts.ContactsFragment
import com.example.android.moviefinder.view.favorites.FavoritesFragment
import com.example.android.moviefinder.view.history.HistoryFragment
import com.example.android.moviefinder.view.home.HomeFragment
import com.example.android.moviefinder.view.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        initBottomNavigation()

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment.newInstance())
        }

        initNetworkCallback()
    }

    private val binding: MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initNetworkCallback() {
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(localMainReceiver, IntentFilter(NETWORK_STATUS_INTENT_FILTER))
        NetworkMonitor(application).startNetworkCallback()
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

    override fun onDestroy() {
        super.onDestroy()
        NetworkMonitor(application).stopNetworkCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.search -> Toast.makeText(this, "Searching", Toast.LENGTH_LONG).show()
            R.id.contacts -> replaceFragment(ContactsFragment.newInstance(), true)
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun initBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment.newInstance())
                R.id.favorites -> replaceFragment(FavoritesFragment.newInstance())
                R.id.history -> replaceFragment(HistoryFragment.newInstance())
                R.id.settings -> replaceFragment(SettingsFragment.newInstance())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        supportFragmentManager.apply {
            for (i in 0 until backStackEntryCount) popBackStack()
            if (addToBackStack) {
                beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack("")
                    .commit()
            } else {
                beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            }
        }
    }
}