package broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import model.AlarmDatabase
import model.AlarmRepository
import service.AlarmService
import service.RescheduleAlarmService
import java.util.*
import kotlin.coroutines.coroutineContext

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("on receive", "Hello")
        //khi tat nguon dt, tat ca service se bi mat, vi vay can check khi nao dt dc bat tro lai, khi do broadcast ACTION_BOOT_COMPLETED se duoc gui vao receiver,
            //ta co the dung broadcast do de bat lai cac bao thuc truoc do tu Room
        if(Intent.ACTION_BOOT_COMPLETED == intent.action){
            Toast.makeText(context,"Alarm is rescheduled", Toast.LENGTH_LONG).show()
            startRescheduleAlarmService(context)
        }
        else{
            if(!intent.getBooleanExtra("RECURRING",false)){ //neu bao thuc ko lap lai
                startAlarmService(context,intent)
            }
            else{
                Log.e("receiver", "repeat")
                if(todayHasAlarm(intent)){ // neu co bao thuc trong ngay thi moi start service (dung cho truong hop bao thuc lap lai nhieu ngay)
                    startAlarmService(context,intent)
                }
            }
        }
    }

    private fun todayHasAlarm(intent: Intent):Boolean { // check if today has alarm
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        var today:Int = calendar.get(Calendar.DAY_OF_WEEK)
        Log.e("today on receiver", today.toString())
        when(today){
            Calendar.MONDAY -> {
                if(intent.getBooleanExtra("MONDAY",false)) return true
                return false
            }
            Calendar.TUESDAY -> {
                if(intent.getBooleanExtra("TUESDAY",false)) return true
                return false
            }
            Calendar.WEDNESDAY -> {
                if(intent.getBooleanExtra("WEDNESDAY",false)) return true
                return false
            }
            Calendar.THURSDAY -> {
                if(intent.getBooleanExtra("THURSDAY",false)) return true
                return false
            }
            Calendar.FRIDAY -> {
                if(intent.getBooleanExtra("FRIDAY",false)) return true
                return false
            }
            Calendar.SATURDAY -> {
                if(intent.getBooleanExtra("SATURDAY",false)) return true
                return false
            }
            Calendar.SUNDAY -> {
                if(intent.getBooleanExtra("SUNDAY",false)) return true
                return false
            }
        }
        return false
    }

    private fun startRescheduleAlarmService(context: Context){
        var intentService = Intent(context, RescheduleAlarmService::class.java)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(intentService)
        }
        else{
            context.startService(intentService)
        }
    }

    private fun startAlarmService(context: Context, intent: Intent){
        var intentService = Intent(context, AlarmService::class.java)
        intentService.putExtra("LABEL", intent.getStringExtra("LABEL"))
        intentService.putExtra("HOUR",intent.getIntExtra("HOUR",0))
        intentService.putExtra("MINUTE",intent.getIntExtra("MINUTE",0))
        intentService.putExtra("ID",intent.getLongExtra("ID",0))

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(intentService)
        }
        else{
            context.startService(intentService)
        }
    }
}