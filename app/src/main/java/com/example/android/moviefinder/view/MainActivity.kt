package com.example.android.moviefinder.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.MainActivityBinding
import com.example.android.moviefinder.view.favorites.FavoritesFragment
import com.example.android.moviefinder.view.home.HomeFragment
import com.example.android.moviefinder.view.ratings.RatingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment.newInstance())
        }

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
            when(it.itemId) {
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