package star.lut.com.chatdemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import star.lut.com.chatdemo.constants.ValueConstants;
import star.lut.com.chatdemo.dataModels.sendModel.FirebaseTokenSendModel;
import star.lut.com.chatdemo.dataModels.sendModel.LogoutSendModel;
import star.lut.com.chatdemo.db.PreferenceManager;
import star.lut.com.chatdemo.utils.AlarmUtils;
import star.lut.com.chatdemo.utils.TimeFormatter;
import star.lut.com.chatdemo.webService.UserWebServices;
import star.lut.com.chatdemo.webService.WebServiceFactory;

public class AlarmReceiver extends BroadcastReceiver{
    AlarmUtils alarmUtils;
    PreferenceManager manager;
    @Override
    public void onReceive(Context context, Intent intent) {
        manager = new PreferenceManager(context);
        alarmUtils = new AlarmUtils(context);
        try {
            int ALARM_TYPE = intent.getExtras().getInt(ValueConstants.ALARM_TYPE);
            if (ALARM_TYPE == ValueConstants.ALARM_TYPE_OFF){
                logout();
            }else {
                login();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void logout(){
        LogoutSendModel sendModel = new LogoutSendModel();
        sendModel.userId = manager.getUSerID();

        UserWebServices webServices = WebServiceFactory.createRetrofitService(UserWebServices.class);
        webServices.logout(sendModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<Object> response) {
                        if (response.isSuccessful()){
                            alarmUtils.setAlarm(new TimeFormatter().timeinmillies(manager.getNotificationTimeFrom()), ValueConstants.ALARM_TYPE_ON);
                        }
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

    private void login(){
        String token = manager.getToken();
        String userId = manager.getUSerID();

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
                        if (jsonArrayResponse.isSuccessful()){
                            alarmUtils.setAlarm(new TimeFormatter().timeinmillies(manager.getNotificationTimeTo()), ValueConstants.ALARM_TYPE_OFF);
                        }
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
