package model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import broadcastReceiver.AlarmReceiver
import java.util.*

data class Alarm(
        var hour:Int,
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
        var id: Int, // dung de danh dau bao thuc => huy bao thuc dua vao id
        var isOn: Boolean // de set trang thai cho switch
){
    public fun schedule(context: Context){ // ham nay dung de tao bao thuc (core function)

        // dung alarmManager de gui 1 broadcast tu he thong alarm cua dien thoai
        var alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //khoi tao 1 intent de gui broadcast do den receiver va xu li trong do
        val intent= Intent(context, AlarmReceiver::class.java)
        intent.putExtra("HOUR",hour)
        intent.putExtra("MINUTE",minute)
        intent.putExtra("RECURRING",recurring)
        intent.putExtra("MONDAY",monday)
        intent.putExtra("TUESDAY",tuesday)
        intent.putExtra("WEDNESDAY",wednesday)
        intent.putExtra("THURSDAY",thursday)
        intent.putExtra("FRIDAY",friday)
        intent.putExtra("SATURDAY",saturday)
        intent.putExtra("SUNDAY",sunday)
        intent.putExtra("LABEL",label)

        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);

        // lay thoi tu time picker
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis();
        calendar.set(Calendar.HOUR_OF_DAY,hour)
        calendar.set(Calendar.MINUTE,minute)

        // neu thoi gian hien tai lon hon thoi gian dat bao thuc => tang ngay len 1
        if(calendar.timeInMillis <= System.currentTimeMillis()){
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1)
        }
        if(!recurring){
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
            )
        }
        else{
            val daily: Long = 24*60*60*1000
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    daily,
                    pendingIntent
            )
        }
    }
    public fun cancelAlarm(context: Context){
        var alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,id,intent,0)
        alarmManager.cancel(pendingIntent)

    }
    public fun getRecurringDays():String{
        if(!recurring){
            return ""
        }
        var recurDays =""
        if(monday) recurDays+="Mon "
        if(tuesday) recurDays+="Tue "
        if(wednesday) recurDays+="Wed "
        if(thursday) recurDays+="Thu "
        if(friday) recurDays+="Fri "
        if(saturday) recurDays+="Sat "
        if(sunday) recurDays+="Sun "
        return recurDays
    }
}
