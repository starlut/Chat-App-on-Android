package star.lut.com.chatdemo.firebaseServices;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.ExecutionException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import star.lut.com.chatdemo.Base.BaseApplication;
import star.lut.com.chatdemo.R;
import star.lut.com.chatdemo.appModules.chatDetails.presenter.NewMessageReceived;
import star.lut.com.chatdemo.appModules.chatDetails.view.ChatDetailsActivity;
import star.lut.com.chatdemo.appModules.mother.MotherActivity;
import star.lut.com.chatdemo.constants.ApiConstants;
import star.lut.com.chatdemo.constants.ValueConstants;
import star.lut.com.chatdemo.dataModels.sendModel.FirebaseTokenSendModel;
import star.lut.com.chatdemo.db.PreferenceManager;
import star.lut.com.chatdemo.utils.NotificationUtils;
import star.lut.com.chatdemo.webService.UserWebServices;
import star.lut.com.chatdemo.webService.WebServiceFactory;
import timber.log.Timber;

/**
 * Created by kamrulhasan on 20/11/18.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Timber.d(TAG + remoteMessage.getData());

        String current_thread_id = new PreferenceManager(this).getThreadID();

        FirebaseNotification send = new FirebaseNotification();
        send.threadId = remoteMessage.getData().get("thread_id");
        send.title = remoteMessage.getData().get("title");
        send.contentType = remoteMessage.getData().get("content_type");
        send.content = remoteMessage.getData().get("content");
        send.senderName = remoteMessage.getData().get("sender_name");
        send.profilePicture = remoteMessage.getData().get("profile_picture");
        send.senderId = remoteMessage.getData().get("sender_id");
        Timber.d(TAG+send.toString());

        if (current_thread_id.equals(""+send.threadId)){
            Intent intent = new Intent("asdf");
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
        }else {
            sendNotification(send);
        }


    }

    private void sendNotification(FirebaseNotification send) {
        Intent messageIntent = new Intent(this, ChatDetailsActivity.class);
        messageIntent.putExtra(ValueConstants.FIREBASE_NOTIFICATION, true);
        messageIntent.putExtra(ValueConstants.MESSAGE_THREAD_ID, ""+send.threadId);
        messageIntent.putExtra(ValueConstants.USER_OTHER_ID, ""+send.senderId);
        messageIntent.putExtra(ValueConstants.USER_FULL_NAME, send.senderName);
        messageIntent.putExtra(ValueConstants.USER_OTHER_PIC, send.profilePicture);

        messageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, messageIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        String notificationChannelId = getString(R.string.notification_channel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(notificationChannelId, "notificaiotns", NotificationManager.IMPORTANCE_HIGH);

            channel.setDescription("chat_demo app notification_layout_chat channel");
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 1000, 100});
            channel.setLightColor(Color.CYAN);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannelId);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_camera); //icon


        Bitmap senderIcon;

        try {
            senderIcon = Glide.with(this)
                    .asBitmap()
                    .load(ApiConstants.BASE_URL+new PreferenceManager(this).getUserPic())
                    .submit()
                    .get();

            builder.setLargeIcon(senderIcon);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        builder.setContentTitle(""+send.senderName)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(NotificationCompat.PRIORITY_MAX);
        }

        if (send.contentType.equals(ValueConstants.CONTENT_TYPE_MESSAGE)){
            builder.setContentText(send.content);
        }else {
            builder.setContentText(send.title);
        }


//        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(""+send.senderId));


        manager.notify(Integer.parseInt(send.threadId), builder.build());
    }

    @Override
    public void onNewToken(String token) {
        Timber.d("Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        PreferenceManager manager = new PreferenceManager(this);
        manager.setToken(token);
        Timber.d("Refreshed token: "+manager.getToken());

        if (manager.getUserLoggedIn()) {
            sendforstoringtoken(token, manager.getUSerID());
        }

        manager = null;
    }

    private void sendforstoringtoken(String token, String userId) {
        FirebaseTokenSendModel sendModel = new FirebaseTokenSendModel(token, userId);
        UserWebServices webServices = WebServiceFactory.createRetrofitService(UserWebServices.class);

        webServices.storeToken(sendModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<Object> jsonArrayResponse) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
