package com.example.android.moviefinder.view

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun FragmentActivity.showHomeButton() {
    (this as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
}

fun FragmentActivity.hideHomeButton() {
    (this as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
}