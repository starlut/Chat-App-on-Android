package star.lut.com.chatdemo.appModules.sendMessage.presenter;

import android.content.Context;

import com.google.gson.JsonArray;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import star.lut.com.chatdemo.constants.ValueConstants;
import star.lut.com.chatdemo.dataModels.receiveModel.AllUserResponse;
import star.lut.com.chatdemo.dataModels.sendModel.CreateNewMessageSendModel;
import star.lut.com.chatdemo.db.PreferenceManager;
import star.lut.com.chatdemo.webService.ChatWebServices;
import star.lut.com.chatdemo.webService.UserWebServices;
import star.lut.com.chatdemo.webService.WebServiceFactory;
import timber.log.Timber;

/**
 * Created by kamrulhasan on 29/10/18.
 */
public class SendMessagePresenter {
    private Context context;
    private SendMessageViewInterface viewInterface;
    private UserWebServices webServices;
    private PreferenceManager manager;

    public SendMessagePresenter(Context context, SendMessageViewInterface viewInterface) {
        this.context = context;
        this.viewInterface = viewInterface;

        webServices = WebServiceFactory.createRetrofitService(UserWebServices.class);
        manager = new PreferenceManager(context);
    }

    public void getAllUserList() {
        viewInterface.showProgress();

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
                                viewInterface.onUserListFetched(listResponse.body().userInfo);
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

    public void createNewMessage(CreateNewMessageSendModel model){
        ChatWebServices webServices = WebServiceFactory.createRetrofitService(ChatWebServices.class);

        viewInterface.showProgress();

        webServices.createNewMessage(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<JsonArray>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<JsonArray> jsonArrayResponse) {
                        viewInterface.hideProgress();

                        Timber.d(jsonArrayResponse.toString());

                        if (jsonArrayResponse.code() == 200){
                            viewInterface.onMessageSendSuccessFull();
                        }else if (jsonArrayResponse.code() == 409){
                            viewInterface.onMessageExist(model.senderId, model.receiverId);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        viewInterface.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.hideProgress();
                    }
                });
    }
}

