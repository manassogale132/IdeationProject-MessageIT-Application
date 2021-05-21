package com.example.blogit

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.*
import com.example.blogit.Activities.TabbedActivity
import java.util.concurrent.TimeUnit

class EventHandler(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    lateinit var notificationManager: NotificationManager
    lateinit var builder: Notification.Builder
    lateinit var notificationChannel: NotificationChannel
    private val description = "Test WorkManager Notification"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showNotification() {

        val intent = Intent(applicationContext, TabbedActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pendingIntent = PendingIntent.getActivity(applicationContext,0,intent,0)


        notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //sdk version higher than 26
            notificationChannel = NotificationChannel("WorkManagerNotification", description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(applicationContext, "WorkManagerNotification")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("MessageIT")
                .setContentText("Test WorkManager Notification")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        } else {            //sdk version less than 26
            builder = Notification.Builder(applicationContext, "WorkManagerNotification")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("MessageIT")
                .setContentText("Test WorkManager Notification")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }
        notificationManager.notify(101, builder.build())
    }

    /*fun setConstraint(): Constraints {
        val constraints: Constraints = Constraints.Builder()
            .build()
        return constraints
    }*/
}
