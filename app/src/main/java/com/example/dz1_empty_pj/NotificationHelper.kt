package com.example.dz1_empty_pj

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class NotificationHelper {
    companion object {
        fun createNotificationChanel(context: Context, name: String, appName: String, import: Int) {
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(name, appName, import)
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
    }
}