package star.lut.com.chatdemo.appModules.chatThreads.presenter;

import android.content.Context;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import star.lut.com.chatdemo.constants.ValueConstants;
import star.lut.com.chatdemo.dataModels.receiveModel.AllUserResponse;
import star.lut.com.chatdemo.dataModels.receiveModel.MessageThread;
import star.lut.com.chatdemo.db.PreferenceManager;
import star.lut.com.chatdemo.webService.ChatWebServices;
import star.lut.com.chatdemo.webService.UserWebServices;
import star.lut.com.chatdemo.webService.WebServiceFactory;
import timber.log.Timber;

/**
 * Created by kamrulhasan on 28/10/18.
 */
public class MessageThreadPresenter {
    private Context context;
    private MessageThreadViewInterface viewInterface;
    private ChatWebServices webServices;

    public MessageThreadPresenter(Context context, MessageThreadViewInterface viewInterface) {
        this.context = context;
        this.viewInterface = viewInterface;

        webServices = WebServiceFactory.createRetrofitService(ChatWebServices.class);
    }

    public void getAllMessageThread(){
        PreferenceManager manager = new PreferenceManager(context);
        String uid = manager.getUSerID();

        webServices.getAllMessageThread(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<MessageThread>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<MessageThread> messageThreadResponse) {
                        if (messageThreadResponse.isSuccessful()){
                            Timber.d("asdf"+messageThreadResponse.toString());
                            MessageThread messageThread = messageThreadResponse.body();
                            if (messageThread.messageThreadLists != null){
                                viewInterface.onNewThreadListRetrieved(messageThread.messageThreadLists);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getAllUserInfo(){
        viewInterface.showProgress();
        PreferenceManager manager = new PreferenceManager(context);

        UserWebServices webServices = WebServiceFactory.createRetrofitService(UserWebServices.class);

        webServices.getAllUser(ValueConstants.USER_API_ALL_USER, manager.getUSerID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<AllUserResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<AllUserResponse> listResponse) {
                        viewInterface.hideProgress();

                        Timber.d("asdf"+listResponse.toString());

                        if (listResponse.isSuccessful()){
                            Timber.d("asdf"+"success");
                            if (listResponse.body() != null){
                                viewInterface.onNewUserDataFetched(listResponse.body().userInfo);
                                Timber.d("asdf"+listResponse.body().toString());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        viewInterface.hideProgress();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.hideProgress();
                    }
                });
    }
}
