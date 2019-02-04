package star.lut.com.chatdemo.appModules.login.presenter;

import star.lut.com.chatdemo.Base.MvpView;
import star.lut.com.chatdemo.dataModels.receiveModel.LoginResponse;

/**
 * Created by kamrulhasan on 28/10/18.
 */
public interface LoginViewInterface extends MvpView {
    void onUserLoginSuccess(LoginResponse loginResponse);
    void onUserLoginFailed();
    void onTokenSaved();
}
