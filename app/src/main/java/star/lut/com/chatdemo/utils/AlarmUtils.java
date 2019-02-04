package star.lut.com.chatdemo.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import star.lut.com.chatdemo.receiver.AlarmReceiver;

public class AlarmUtils {
    private Context context;
    private Intent intent;

    public AlarmUtils(Context context) {
        this.context = context;
        this.intent = new Intent(context, AlarmReceiver.class);
    }

    public void setAlarm(long time, int alarmId){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(context , alarmId , intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setWindow(AlarmManager.RTC_WAKEUP, time, 3000, pendingIntent);
    }
}
