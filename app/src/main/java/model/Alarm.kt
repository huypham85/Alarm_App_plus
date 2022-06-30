package model

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import broadcastReceiver.AlarmReceiver
import java.util.*

@Entity(tableName = "alarm_table")
data class Alarm(
    var hour: Int,
    var minute: Int,
    var recurring: Boolean,
    var monday: Boolean,
    var tuesday: Boolean,
    var wednesday: Boolean,
    var thursday: Boolean,
    var friday: Boolean,
    var saturday: Boolean,
    var sunday: Boolean,
    var label: String,
    var isOn: Boolean,
    var timeCreated: Long,
    @PrimaryKey var id: Long
) {

    @SuppressLint("UnspecifiedImmutableFlag")
    fun schedule(context: Context) { // ham nay dung de tao bao thuc (core function)
        Log.e("ALarm", "schedule")
        // dung alarmManager de gui 1 broadcast tu he thong alarm cua dien thoai
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //khoi tao 1 intent de gui broadcast do den receiver va xu li trong do
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("HOUR", hour)
            putExtra("MINUTE", minute)
            putExtra("RECURRING", recurring)
            putExtra("MONDAY", monday)
            putExtra("TUESDAY", tuesday)
            putExtra("WEDNESDAY", wednesday)
            putExtra("THURSDAY", thursday)
            putExtra("FRIDAY", friday)
            putExtra("SATURDAY", saturday)
            putExtra("SUNDAY", sunday)
            putExtra("LABEL", label)
            putExtra("ID", id)
        }
        val pendingIntent = id.let { PendingIntent.getBroadcast(context, it.toInt(), intent, 0) }

        // lay thoi tu time picker
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        // neu thoi gian hien tai lon hon thoi gian dat bao thuc => tang ngay len 1
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1)
        }

        // tao bao thuc tren dien thoai
        if (!recurring) {
            Log.e("Alarm", "!recurring")
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            Log.e("Alarm", "repeat")
            val daily: Long = 24 * 60 * 60 * 1000
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                daily,
                pendingIntent
            )
        }
        Log.e("id schedule", id.toString())
        Log.e("schedule pending intent", pendingIntent.toString())
        Log.e("Schedule Alarm context", context.toString())
        this.isOn = true
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = id.let { PendingIntent.getBroadcast(context, it.toInt(), intent, 0) }
        Log.e("cancel pending intent", pendingIntent.toString())
        Log.e("id cancel", id.toString())
        Log.e("Cancel Alarm context", context.toString())
        alarmManager.cancel(pendingIntent)
        this.isOn = false
    }

    fun getRecurringDays(): String {
        if (!recurring) {
            return ""
        }
        var recurDays = ""
        if (monday) recurDays += "Mon "
        if (tuesday) recurDays += "Tue "
        if (wednesday) recurDays += "Wed "
        if (thursday) recurDays += "Thu "
        if (friday) recurDays += "Fri "
        if (saturday) recurDays += "Sat "
        if (sunday) recurDays += "Sun "
        return recurDays
    }
}
