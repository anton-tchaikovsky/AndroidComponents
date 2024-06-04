package com.tchaikovsky.androidcomponents

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import com.tchaikovsky.androidcomponents.ContactsService.Companion.CONTACTS_ACTION
import com.tchaikovsky.androidcomponents.ContactsService.Companion.KEY_CONTACTS
import com.tchaikovsky.androidcomponents.MediaService.Companion.KEY_FOR_MEDIA_SERVICE_INTENT

class SecondActivity : AppCompatActivity() {

    private var contactsService: ContactsService? = null

    private val contactsServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            Log.d("@@@", "onContactsServiceConnection")
            contactsService = (binder as ContactsService.ContactsBinder).getContactsService()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.d("@@@", "onContactsServiceDisconnected")
            contactsService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        initResultForFirstActivity()
        initStartMediaService()
        initBindContactsService()
        initStartContactsService()
        initContactReceiver()
    }

    private fun initResultForFirstActivity() {
        findViewById<TextView>(R.id.second_activity_text_view).text =
            intent.getStringExtra(FirstActivity.INPUT_KEY_FOR_ACTIVITY_RESULT)
        findViewById<AppCompatButton>(R.id.second_ok_button).setOnClickListener {
            setResult(
                Activity.RESULT_OK,
                Intent().putExtra(
                    FirstActivity.OUTPUT_KEY_ANSWER_FOR_ACTIVITY_RESULT,
                    findViewById<EditText>(R.id.second_activity_edit_text).text.toString()
                )
            )
            finish()
        }
    }

    private fun initStartMediaService() {
        val mediaIntent = Intent(this, MediaService::class.java).putExtra(
            KEY_FOR_MEDIA_SERVICE_INTENT,
            getString(R.string.play)
        )
        findViewById<AppCompatButton>(R.id.play_media_button).setOnClickListener {
            startService(mediaIntent)
        }
        findViewById<AppCompatButton>(R.id.stop_media_button).setOnClickListener {
            stopService(mediaIntent)
        }
    }

    private fun initBindContactsService() {
        val contactsIntent = Intent(this, ContactsService::class.java)
        bindService(contactsIntent, contactsServiceConnection, BIND_AUTO_CREATE)
        findViewById<AppCompatButton>(R.id.get_contact_button).setOnClickListener {
            findViewById<TextView>(R.id.contact_text_view).text = contactsService?.getContact(0)
        }
    }

    private fun initStartContactsService() {
        findViewById<AppCompatButton>(R.id.transfer_contacts_button).setOnClickListener {
            startService(Intent(this, ContactsService::class.java))
        }
    }

    private fun initContactReceiver() {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                p1?.getStringArrayExtra(KEY_CONTACTS)?.let {
                    setResult(
                        Activity.RESULT_OK,
                        Intent().putExtra(FirstActivity.OUTPUT_KEY_CONTACTS_FOR_ACTIVITY_RESULT, it)
                    )
                    finish()
                }
            }
        }
        registerReceiver(receiver, IntentFilter(CONTACTS_ACTION))
    }

    override fun onDestroy() {
        unbindService(contactsServiceConnection)
        super.onDestroy()
    }
}