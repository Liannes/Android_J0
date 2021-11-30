package com.example.dz1_empty_pj.broadreciver

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.dz1_empty_pj.MainActivity
import com.example.dz1_empty_pj.R
import com.example.dz1_empty_pj.data.SetContact.broadCast
import java.util.*

class BroadReceiver : BroadcastReceiver() {

    companion object {
        private const val FRAGMENT = "contactDetails"
        private const val CONTACT_ID = "contactId"
        private const val CHANNEL_ID = "birthday"
        private const val NAME_CHANNEL = "birthdayChannel"
        private const val MEMO = "ID"
        private const val TAG = "BROADCAST"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "OnReceive Context = $context")
        Log.d(TAG, "OnReceive Intent = $intent")

        val contactId = intent?.getStringExtra(CONTACT_ID).toString()
        val memo = intent?.getStringExtra(MEMO).toString()

        showNotification(context, contactId, memo)
    }

    private fun showNotification(context: Context?, contactId: String, memo: String) {
        Log.d(TAG, "showNotification")
        val resultIntent = Intent(context, MainActivity::class.java)
        resultIntent.putExtra(FRAGMENT, CONTACT_ID)
        resultIntent.putExtra(MEMO, memo)

        val stackBinder = TaskStackBuilder.create(context)
        stackBinder.addNextIntent(resultIntent)

        val resultPendingIntent: PendingIntent? =
            stackBinder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = context?.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic__giif_birthday)
                .setContentTitle(context.getString(R.string.title_notification))
                .setContentText(memo)
                .setContentIntent(resultPendingIntent)
                .setChannelId(CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
        }

        val notificationManager: NotificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, NAME_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(1, notification?.build())
        setAlarm(context, contactId, memo)
    }

    private fun setAlarm(context: Context, contactId: String, reminder: String) {
        Log.d(TAG, "setAlarm")
        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(broadCast)
        intent.putExtra(CONTACT_ID, contactId)
        intent.putExtra(MEMO, reminder)

        val birthday: Calendar = GregorianCalendar()
        birthday.add(Calendar.YEAR, 1)

        val alarmIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.set(AlarmManager.RTC, birthday.timeInMillis, alarmIntent)
    }
}