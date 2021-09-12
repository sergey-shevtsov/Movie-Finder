package com.example.android.moviefinder.view

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import java.util.*

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

fun View.showSnackBarMessage(stringId: Int, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, resources.getString(stringId), length).show()
}

fun String.formatDate(): String {
    val year = this.subSequence(0, 4)
    val month = this.subSequence(5, 7)
    val day = this.subSequence(8, 10)
    return "$day.$month.$year"
}

fun Int.getFormatDuration(): String {
    val hours = (this / 60).formatNum()
    val minutes = (this % 60).formatNum()
    return String.format(Locale.getDefault(), "%s:%s", hours, minutes)
}

fun Int.formatNum(): String {
    return if (this < 10) "0$this"
    else this.toString()
}