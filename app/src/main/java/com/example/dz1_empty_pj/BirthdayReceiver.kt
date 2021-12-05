package com.example.dz1_empty_pj

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.*

const val FRAGMENT_ID = "FRAGMENT_ID"
const val CONTACT_ID = "contact_Id"
const val CONTACT_NAME = "CONTACT_NAME"
const val FRAGMENT_LAYOUT = "contactDetails"
const val TAG = "RECEIVER"

class BirthdayReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "ON_RECEIVE")
        // Создаем менаджера уведомлений
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Создаем канал
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(
                    context.getString(R.string.notification_channel_birthday_id),
                    context.getString(R.string.notification_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        // Обозначаем уведомление
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.putExtra(FRAGMENT_ID, FRAGMENT_LAYOUT)
        Log.d(TAG, "notificationIntent = $notificationIntent")
        // Обновляем
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        // Структура уведомления
        val notification = NotificationCompat.Builder(
            context,
            context.getString(R.string.notification_channel_birthday_id)
        )
            .setContentTitle(context.getString(R.string.app_name))
            .setChannelId(context.getString(R.string.notification_channel_birthday_id))
            .setContentText(
                context.getString(R.string.notification_text)+ " " + intent.getStringExtra(
                    CONTACT_NAME
                )
            )
            .setSmallIcon(R.drawable.ic__giif_birthday)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val contactId = intent.getIntExtra(CONTACT_ID, 0)
        Log.d(TAG, "ContactId = $contactId")
        val birthday = calendarBirthday(Calendar.getInstance())

        notificationManager.notify(contactId, notification)
        // Обновление
        val movePendingIntent = PendingIntent.getBroadcast(
            context,
            contactId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, birthday, movePendingIntent)
    }

    private fun calendarBirthday(calendar: Calendar): Long {
        Log.d(TAG, "CALENDAR")
        val notificationCalendar = calendar as GregorianCalendar
        notificationCalendar[Calendar.DATE] = calendar[Calendar.DATE]
        notificationCalendar[Calendar.MONTH] = calendar[Calendar.MONTH]
        notificationCalendar[Calendar.YEAR] = calendar[Calendar.YEAR]
        notificationCalendar[Calendar.HOUR] = 0
        notificationCalendar[Calendar.MINUTE] = 0
        notificationCalendar[Calendar.SECOND] = 0

        if (System.currentTimeMillis() > notificationCalendar.timeInMillis)
            notificationCalendar.add(Calendar.YEAR, 1)
        Log.d(TAG, "notificationCalendar $notificationCalendar")
        return calendar.timeInMillis
    }
}