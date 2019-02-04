package star.lut.com.chatdemo.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import star.lut.com.chatdemo.constants.ValueConstants;
import star.lut.com.chatdemo.db.PreferenceManager;
import star.lut.com.chatdemo.utils.AlarmUtils;
import star.lut.com.chatdemo.utils.TimeFormatter;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            //set alarm here
            PreferenceManager manager = new PreferenceManager(context);
            AlarmUtils alarmUtils = new AlarmUtils(context);
            long from = new TimeFormatter().timeinmilliesAccurate(manager.getNotificationTimeFrom());

            if (from > System.currentTimeMillis()) {
                alarmUtils.setAlarm(new TimeFormatter().timeinmillies(manager.getNotificationTimeFrom()), ValueConstants.ALARM_TYPE_ON);
            } else {
                alarmUtils.setAlarm(new TimeFormatter().timeinmillies(manager.getNotificationTimeTo()), ValueConstants.ALARM_TYPE_OFF);
            }
        }

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
