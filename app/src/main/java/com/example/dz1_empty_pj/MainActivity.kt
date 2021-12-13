package com.example.dz1_empty_pj

import android.app.NotificationManager
import android.content.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.example.dz1_empty_pj.`object`.Constants.APP_NAME
import com.example.dz1_empty_pj.`object`.Constants.CHANNEL_ID
import com.example.dz1_empty_pj.`object`.Constants.CONTACTS_ID
import com.example.dz1_empty_pj.`object`.Constants.TAG_SERVICE_MAIN
import com.example.dz1_empty_pj.databinding.ActivityMainBinding
import com.example.dz1_empty_pj.fragment.ContactDetailsFragment
import com.example.dz1_empty_pj.fragment.ContactListFragment
import com.example.dz1_empty_pj.service.ContactInterface
import com.example.dz1_empty_pj.service.GetService
import com.example.dz1_empty_pj.service.ServerContact

class MainActivity : AppCompatActivity(R.layout.activity_main), ContactInterface, GetService {
    private lateinit var binding: ActivityMainBinding
    private lateinit var broadcastReceiver: BirthdayReceiver

    private var serverContact: ServerContact? = null

    private var contactBound: Boolean = false
    private var savedFirstFragment: Boolean? = null


    private val serviceConnected = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG_SERVICE_MAIN, "Connect")

            val binder = service as ServerContact.ContactServiceBinder
            serverContact = binder.getService()
            contactBound = true

            val contactId = intent.getStringExtra(CONTACTS_ID)
            Log.d(TAG_SERVICE_MAIN,"contactId = $contactId")

            if (savedFirstFragment == true) {
                navigationToContactListFragment()
                contactId?.let { navigationToContactDetailsFragment(it) }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG_SERVICE_MAIN, "Disconnect")
            contactBound = false
        }
    }

    override fun getStatusBound(): Boolean = contactBound

    override fun getService(): ServerContact? = serverContact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationHelper.createNotificationChanel(
                this,
                CHANNEL_ID,
                APP_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        }

        savedFirstFragment = savedInstanceState == null

        val intent = Intent(this, ServerContact::class.java)
        bindService(intent, serviceConnected, BIND_AUTO_CREATE)

        broadcastReceiver = BirthdayReceiver()
        registerReceiver(broadcastReceiver, IntentFilter(ALARM_SERVICE))

    }

    private fun navigationToContactListFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(binding.mainFragment.id, ContactListFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun navigationToContactDetailsFragment(contactId: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.mainFragment.id, ContactDetailsFragment.newInstance(contactId))
        transaction.addToBackStack("DetailContact")
        transaction.commit()
    }

    override fun onDestroy() {
        unbindService(serviceConnected)
        super.onDestroy()
    }
}