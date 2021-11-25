package com.example.dz1_empty_pj

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.example.dz1_empty_pj.databinding.ActivityMainBinding
import com.example.dz1_empty_pj.fragment.ContactDetailsFragment
import com.example.dz1_empty_pj.fragment.ContactListFragment
import com.example.dz1_empty_pj.service.GetService
import com.example.dz1_empty_pj.service.ServerContact

class MainActivity : AppCompatActivity(R.layout.activity_main), ContactInterface, GetService {

    private lateinit var binding: ActivityMainBinding
    private var serverContact: ServerContact? = null
    private var contactBound: Boolean = false
    private var savedFirstFragment: Boolean? = null

    private val serviceConnected = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as ServerContact.ContactServiceBinder
            serverContact = binder.getService()
            contactBound = true
            if (savedFirstFragment == true) navigationToContactListFragment()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            contactBound = false
        }
    }

    override fun getStatusBound(): Boolean = contactBound

    override fun getService(): ServerContact? = serverContact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        savedFirstFragment = savedInstanceState == null
        val intent = Intent(this, ServerContact::class.java)
        bindService(intent, serviceConnected, BIND_AUTO_CREATE)
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