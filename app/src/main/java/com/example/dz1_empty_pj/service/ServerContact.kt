package com.example.dz1_empty_pj.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.dz1_empty_pj.data.Contact
import com.example.dz1_empty_pj.data.SetContact.setContact
import kotlinx.coroutines.delay
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class ServerContact : Service() {
    private var binder = ContactServiceBinder()

    inner class ContactServiceBinder : Binder() {
        fun getService() = this@ServerContact
    }

    override fun onBind(intent: Intent?): IBinder = binder

    suspend fun getContactList(): List<Contact> {
        delay(2000)
        return setContact
    }

    suspend fun getContactDetails(contactId: String): Contact? {
        delay(2000)
        val correctId = setContact.find {
            it.contactId == contactId
        }
        return correctId
    }
}