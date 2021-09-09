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

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar
        .make(
            this,
            text,
            length
        )
        .setAction(actionText) { action(this) }
        .show()
}

fun FragmentActivity.showHomeButton() {
    (this as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
}

fun FragmentActivity.hideHomeButton() {
    (this as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
}