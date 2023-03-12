package it.unipi.gardenfit.util

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import it.unipi.gardenfit.GardenFitActivity
import it.unipi.gardenfit.R

// Notification ID.
private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0
private const val FLAGS = 0


/**
 * Builds and delivers the notification.
 *
 * @param context Activity context.
 */
@SuppressLint("UnspecifiedImmutableFlag")
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    // Creates the content intent for the notification, which launches the activity
    val contentIntent = Intent(applicationContext, GardenFitActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val cactusImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.cactus
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(cactusImage)
        .bigLargeIcon(null)

    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.plant_channel)
    )
        .setSmallIcon(R.drawable.cactus)
        .setContentTitle(applicationContext.getString(R.string.moist_reminder))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setStyle(bigPicStyle)
        .setLargeIcon(cactusImage)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}


/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}