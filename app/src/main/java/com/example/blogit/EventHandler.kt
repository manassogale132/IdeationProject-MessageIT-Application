package com.example.blogit

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.*
import com.example.blogit.Activities.TabbedActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.coroutines.suspendCoroutine

class EventHandler(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    lateinit var notificationManager: NotificationManager
    lateinit var builder: Notification.Builder
    lateinit var notificationChannel: NotificationChannel
    private val description = "Test WorkManager Notification"


    override suspend fun doWork(): Result {
        return try {
            setStatusExpiration()
            Result.success()
        } catch (exception : Exception) {
            Result.failure()
        }
    }

    private suspend fun setStatusExpiration() = suspendCoroutine<Unit> {continuation ->
        val statusId = inputData.getString("StatusId") ?: throw IllegalStateException("Status id is not found")
        FirebaseFirestore.getInstance().collection("Status Info").document(statusId)
            .delete().addOnSuccessListener {
            continuation.resumeWith(kotlin.Result.success(Unit))
        }
            .addOnFailureListener {
                continuation.resumeWith(kotlin.Result.failure(it))
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showNotification() {
        Log.i("Test", "showNotification: Working")
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

}
