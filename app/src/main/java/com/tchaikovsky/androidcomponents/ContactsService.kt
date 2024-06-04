package com.tchaikovsky.androidcomponents

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
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
            putExtra(KEY_CONTACTS,contacts ?: loadContacts())
        }
        sendBroadcast(contactsIntent)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d("@@@", "onBindContactsService")
        return ContactsBinder()
    }

    private fun loadContacts()=
        arrayOf("Антон", "Петр").also {
            contacts = it
        }

    fun getContact (position:Int): String = contacts?.get(position) ?: loadContacts()[position]

    override fun onDestroy() {
        Log.d("@@@", "onDestroyContactsService")
        super.onDestroy()
    }

    inner class ContactsBinder: Binder(){
        fun getContactsService() = this@ContactsService
    }

    companion object{
       internal const val KEY_CONTACTS = "KeyContacts"
       internal const val CONTACTS_ACTION = "com.tchaikovsky.androidcomponents.CONTACTS_ACTION"
    }
}