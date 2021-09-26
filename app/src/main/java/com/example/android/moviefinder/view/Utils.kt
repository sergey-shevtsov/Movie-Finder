package com.example.android.moviefinder.view

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

const val CHILD_MODE_KEY = "ChildModeKey"

fun Activity.isChildMode(): Boolean? {
    return this.getPreferences(Context.MODE_PRIVATE)
        ?.getBoolean(CHILD_MODE_KEY, false)
}

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

fun String.getStringFormat(vararg args: Any?): String =
    String.format(Locale.getDefault(), this, *args)

fun Long.formatToPattern(pattern: String): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(this)
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)