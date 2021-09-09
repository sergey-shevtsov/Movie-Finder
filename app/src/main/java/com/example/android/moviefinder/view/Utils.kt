package com.example.android.moviefinder.view

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar

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

fun View.showSnackBarMessage(stringId: Int) {
    Snackbar.make(this, resources.getString(stringId), Snackbar.LENGTH_SHORT).show()
}