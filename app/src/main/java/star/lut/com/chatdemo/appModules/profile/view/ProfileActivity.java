package star.lut.com.chatdemo.appModules.profile.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import star.lut.com.chatdemo.Base.BaseActivity;
import star.lut.com.chatdemo.R;
import star.lut.com.chatdemo.appModules.profile.presenter.ProfileViewInterface;
import star.lut.com.chatdemo.appModules.profile.presenter.ProfileViewPresenter;
import star.lut.com.chatdemo.appModules.splash.SplashActivity;
import star.lut.com.chatdemo.constants.ApiConstants;
import star.lut.com.chatdemo.dataModels.receiveModel.Profile;
import star.lut.com.chatdemo.db.PreferenceManager;
import star.lut.com.chatdemo.utils.ProgressBarHandler;

public class ProfileActivity extends BaseActivity implements ProfileViewInterface {
    private ProfileViewPresenter presenter;
    private ActionBar actionBar;
    private PreferenceManager manager;
    private ProgressBarHandler handler;

    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.toolbarText) public TextView toolbarText;
    @BindView(R.id.ivProPic) public ImageView ivProPic;
    @BindView(R.id.tvName) public TextView tvName;
    @BindView(R.id.tvPosition) public TextView tvPostion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new ProfileViewPresenter(this, this);

        actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        }

        setTitle("");

        toolbarText.setText("Profile");

        manager = new PreferenceManager(this);
        handler = new ProgressBarHandler(this);

        initView();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_profile;
    }


    private void initView(){
        Glide.with(this)
                .load(ApiConstants.BASE_URL + manager.getUserPic())
                .into(ivProPic);

        tvName.setText("Full Name: "+ manager.getFullName());
        tvPostion.setText("Position: "+manager.getUserPosition());
    }

    @OnClick(R.id.btnLogout)
    public void btnLogout(){
        String userid = new PreferenceManager(this).getUSerID();

        presenter.logout(userid);
    }

    @OnClick({R.id.ivEdit , R.id.ivProPic})
    public void editProPic(){
        if (checkPermission()) {
            presenter.selectPicture();
        } else {
            permissionRequest();
        }
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            // Permission has already been granted
            return true;
        }
    }

    private void permissionRequest() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1234);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1234: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.selectPicture();
                } else {

                }
                return;
            }
        }
    }

    @Override
    public void showProgress() {
        handler.showProgress();
    }

    @Override
    public void hideProgress() {
        handler.hideProgress();
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
        handler.hideProgress();
    }

    @Override
    public void onProfilePicUpdated() {
        initView();
    }

    @Override
    public void onLogout() {
        new PreferenceManager(this).clearPref();
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        ActivityCompat.finishAffinity(this);
    }
}
