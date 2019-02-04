package star.lut.com.chatdemo.appModules.splash;

import android.content.Intent;
import android.os.Bundle;

import star.lut.com.chatdemo.Base.BaseActivity;
import star.lut.com.chatdemo.R;
import star.lut.com.chatdemo.appModules.login.view.LoginActivity;
import star.lut.com.chatdemo.appModules.mother.MotherActivity;
import star.lut.com.chatdemo.db.PreferenceManager;
import timber.log.Timber;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager manager = new PreferenceManager(this);
        Timber.d("Refreshed token: "+manager.getToken());
        if (manager.getUserLoggedIn()){
            Intent intent= new Intent(this, MotherActivity.class);
            startActivity(intent);
            intent = null;
        }else {
            Intent intent= new Intent(this, LoginActivity.class);
            startActivity(intent);
            intent = null;
        }
        finish();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_splash;
    }
}
