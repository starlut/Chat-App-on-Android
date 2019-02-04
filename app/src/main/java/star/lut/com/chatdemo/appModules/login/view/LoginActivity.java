package star.lut.com.chatdemo.appModules.login.view;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import star.lut.com.chatdemo.Base.BaseActivity;
import star.lut.com.chatdemo.R;
import star.lut.com.chatdemo.db.PreferenceManager;
import star.lut.com.chatdemo.appModules.login.presenter.LoginPresenter;
import star.lut.com.chatdemo.appModules.login.presenter.LoginViewInterface;
import star.lut.com.chatdemo.appModules.mother.MotherActivity;
import star.lut.com.chatdemo.dataModels.receiveModel.LoginResponse;
import star.lut.com.chatdemo.dataModels.sendModel.LoginSendModel;
import star.lut.com.chatdemo.utils.ProgressBarHandler;
import timber.log.Timber;

public class LoginActivity extends BaseActivity implements LoginViewInterface {
    @BindView(R.id.tilUserName) public TextInputLayout tilUserName;
    @BindView(R.id.tilPassword) public TextInputLayout tilPassword;
    private String username, password;
    private LoginPresenter presenter;
    private ProgressBarHandler progressBarHandler;
    private PreferenceManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = new PreferenceManager(this);

        presenter = new LoginPresenter(this, this);

        progressBarHandler = new ProgressBarHandler(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }


    @OnClick(R.id.btnLogin)
    public void login(){
        if (validatePassword() | validateUserName()){
            LoginSendModel model = new LoginSendModel();
            model.username = username;
            model.password = password;
            presenter.logInUser(model);
        }else{
            password = null;
            username = null;
        }
    }

    private boolean validateUserName(){
        username = tilUserName.getEditText().getText().toString().trim();
        if (username.isEmpty()){
            tilUserName.setErrorEnabled(true);
            tilUserName.setError("UserName");
            return false;
        }else if(username.length() > 15){
            tilUserName.setErrorEnabled(true);
            tilUserName.setError("error");
            return false;
        }
        else {
            tilUserName.setErrorEnabled(false);
            tilUserName.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        password = tilPassword.getEditText().getText().toString().trim();
        if (password.isEmpty()){
            tilPassword.setErrorEnabled(true);
            tilPassword.setError("Password");
            return false;
        } else if (password.length() < 4){
            tilPassword.setErrorEnabled(true);
            tilPassword.setError("Password");
            return false;
        } else {
            tilPassword.setErrorEnabled(false);
            tilPassword.setError(null);
            return true;
        }
    }

    @Override
    public void onUserLoginSuccess(LoginResponse loginResponse) {
        Timber.d("localadfa"+loginResponse.toString());

        manager.setUserLoggedIn(true);
        manager.setUserID(loginResponse.id);
        manager.setUserName(loginResponse.name);
        manager.setUserPic(loginResponse.pic);
        manager.setPassword(password);
        manager.setPosition(loginResponse.position);
        manager.setFullName(loginResponse.fullName);

        presenter.setTokenToServer();
    }

    @Override
    public void onUserLoginFailed() {
        Toast.makeText(this, "Username or password is wrong", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTokenSaved() {
        if (manager.getUserLoggedIn()){
            Intent intent = new Intent(this, MotherActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void showProgress() {
        progressBarHandler.showProgress();
    }

    @Override
    public void hideProgress() {
        progressBarHandler.hideProgress();
    }

    @Override
    public void onError(Throwable t) {

    }
}
