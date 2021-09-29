package com.example.android.moviefinder.viewmodel

import android.app.Activity
import android.database.Cursor
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviefinder.model.PhonebookContact

class ContactsViewModel(
    private val contactsLiveData: MutableLiveData<AppState> = MutableLiveData()
) : ViewModel() {
    val liveData: LiveData<AppState> = contactsLiveData

    fun getContacts(activity: Activity) {
        contactsLiveData.value = AppState.Loading

        val cr = activity.contentResolver

        val cursor: Cursor? = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )

        val contacts = mutableListOf<PhonebookContact>()

        cursor?.let {
            for (i in 0..cursor.count) {

                if (cursor.moveToPosition(i)) {

                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    var phoneNumber: String? = null

                    if (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                            .toInt() > 0
                    ) {

                        val phoneCursor = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                            arrayOf(id),
                            null
                        )

                        phoneCursor?.let {
                            if (it.moveToFirst()) {
                                phoneNumber =
                                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                it.close()
                            }
                        }

                    }

                    phoneNumber?.let { contacts.add(PhonebookContact(name, it)) }

                }

            }

            cursor.close()
        }

        contactsLiveData.postValue(AppState.Success(contacts))
    }
}