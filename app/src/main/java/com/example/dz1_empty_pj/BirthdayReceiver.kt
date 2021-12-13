package com.example.dz1_empty_pj

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.dz1_empty_pj.`object`.Constants.CHANNEL_ID
import com.example.dz1_empty_pj.`object`.Constants.CONTACTS_ID
import com.example.dz1_empty_pj.`object`.Constants.CONTACT_NAME
import com.example.dz1_empty_pj.`object`.Constants.TAG_RECEIVER

class BirthdayReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Создаем менаджера уведомлений
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val contactId = intent.getStringExtra(CONTACTS_ID)
        Log.d(TAG_RECEIVER, "contactId = $contactId")
        // Обозначаем уведомление
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.putExtra(CONTACTS_ID, contactId)

        // Обновляем
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        // Структура уведомления
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.notification_name))
            .setChannelId(context.getString(R.string.notification_channel_birthday_id))
            .setContentText(context.getString(R.string.notification_text)+ " " +
                        intent.getStringExtra(CONTACT_NAME))
            .setSmallIcon(R.drawable.ic__giif_birthday)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(contactId.hashCode(), notification)
    }
}