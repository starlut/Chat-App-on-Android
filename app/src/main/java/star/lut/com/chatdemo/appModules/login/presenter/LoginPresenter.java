package star.lut.com.chatdemo.appModules.login.presenter;

import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import star.lut.com.chatdemo.dataModels.receiveModel.LoginResponse;
import star.lut.com.chatdemo.dataModels.sendModel.FirebaseTokenSendModel;
import star.lut.com.chatdemo.dataModels.sendModel.LoginSendModel;
import star.lut.com.chatdemo.db.PreferenceManager;
import star.lut.com.chatdemo.webService.LoginWebService;
import star.lut.com.chatdemo.webService.UserWebServices;
import star.lut.com.chatdemo.webService.WebServiceFactory;
import timber.log.Timber;

/**
 * Created by kamrulhasan on 28/10/18.
 */
public class LoginPresenter {
    private Context context;
    private LoginViewInterface viewInterface;
    private LoginWebService webService;

    public LoginPresenter(Context context, LoginViewInterface viewInterface) {
        this.context = context;
        this.viewInterface = viewInterface;

        webService = WebServiceFactory.createRetrofitService(LoginWebService.class);
    }

    public void logInUser(LoginSendModel loginSendModel){
        viewInterface.showProgress();

        Timber.d("asdf" + loginSendModel);
        webService.loginUser(loginSendModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<LoginResponse>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<LoginResponse>> loginResponseResponse) {
                        viewInterface.hideProgress();
                        Timber.d(loginResponseResponse.toString());
                        if (loginResponseResponse.isSuccessful()){
                            viewInterface.onUserLoginSuccess(loginResponseResponse.body().get(0));
                        }else {
                            viewInterface.onUserLoginFailed();
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

    public void setTokenToServer(){
        PreferenceManager manager = new PreferenceManager(context);
        String token = manager.getToken();
        if(token.equals("-1")){
            token = FirebaseInstanceId.getInstance().getToken();
        }
        String userId = manager.getUSerID();

        FirebaseTokenSendModel firebaseTokenSendModel = new FirebaseTokenSendModel(token, userId);

        UserWebServices webServices = WebServiceFactory.createRetrofitService(UserWebServices.class);

        Timber.d(firebaseTokenSendModel.toString());

        webServices.storeToken(firebaseTokenSendModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<Object> jsonArrayResponse) {
                        Timber.d(jsonArrayResponse.toString());
                        if (jsonArrayResponse.isSuccessful()){
                            viewInterface.onTokenSaved();
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
