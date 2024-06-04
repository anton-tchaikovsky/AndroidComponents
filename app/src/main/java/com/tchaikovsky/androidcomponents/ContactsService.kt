package com.tchaikovsky.androidcomponents

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.provider.ContactsContract
import android.util.Log

class ContactsService : Service() {

    private var contacts: Array<String>? = null

    override fun onCreate() {
        Log.d("@@@", "onCreateContactsService")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("@@@", "onStartCommandContactsService")
        val contactsIntent = Intent(CONTACTS_ACTION).apply {
            putExtra(KEY_CONTACTS, contacts ?: loadContacts())
        }
        sendBroadcast(contactsIntent)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d("@@@", "onBindContactsService")
        return ContactsBinder()
    }

    private fun loadContacts(): Array<String> {
        val contactsList = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val cursorContacts =
                contentResolver.query(
                    ContactsContract.CommonDataKinds
                        .Phone.CONTENT_URI,
                    arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME),
                    null,
                    null
                )
            cursorContacts?.run {
                val index = getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                if (index >= 0) {
                    while (moveToNext()) {
                        contactsList.add(getString(index))
                    }
                }
                close()
            }
        } else {
            contactsList.add( "Антон")
            contactsList.add( "Петр")
        }

        return contactsList.toTypedArray().also {
            contacts = it
        }
    }


    fun getContact(position: Int): String = contacts?.get(position) ?: loadContacts()[position]

    override fun onDestroy() {
        Log.d("@@@", "onDestroyContactsService")
        super.onDestroy()
    }

    inner class ContactsBinder : Binder() {
        fun getContactsService() = this@ContactsService
    }

    companion object {
        internal const val KEY_CONTACTS = "KeyContacts"
        internal const val CONTACTS_ACTION = "com.tchaikovsky.androidcomponents.CONTACTS_ACTION"
    }
}