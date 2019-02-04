package star.lut.com.chatdemo.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.Calendar;

import star.lut.com.chatdemo.R;
import star.lut.com.chatdemo.appModules.mother.MotherActivity;
import star.lut.com.chatdemo.constants.ValueConstants;
import star.lut.com.chatdemo.firebaseServices.FirebaseNotification;
import timber.log.Timber;

/**
 * Created by kamrulhasan on 22/11/18.
 */
public class NotificationUtils {
    Context context;

    public NotificationUtils(Context context) {
        this.context = context;
    }

    public void createNotification(FirebaseNotification send){
        Intent messageIntent = new Intent(context, MotherActivity.class);
        messageIntent.putExtra(ValueConstants.FIREBASE_NOTIFICATION, true);
        messageIntent.putExtra(ValueConstants.MESSAGE_THREAD_ID, send.threadId);
        messageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, messageIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String notificationChannelId = context.getString(R.string.notification_channel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(notificationChannelId, "notificaiotns", NotificationManager.IMPORTANCE_HIGH);

            channel.setDescription("chat_demo app notification_layout_chat channel");
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 1000, 100});
            channel.setLightColor(Color.CYAN);
            manager.createNotificationChannel(channel);
        }

        RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.notification_layout_chat);
        Timber.d("notificationsdf "+notificationLayout.getLayoutId());
        notificationLayout.setTextViewText(R.id.tvSenderName, ""+send.senderId);
//        notificationLayout.setImageViewUri(R.id.ivPic, Uri.parse(ApiConstants.BASE_URL+new PreferenceManager(context).getUserPic()));
        notificationLayout.setTextViewText(R.id.tvContent, send.content);
        notificationLayout.setTextViewText(R.id.tvTime, Calendar.getInstance().getTime().toString());

        Notification notification = new NotificationCompat.Builder(context, notificationChannelId)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setSmallIcon(R.drawable.ic_camera)
                .build();
        //change small icon

        manager.notify(1, notification);
    }
}
