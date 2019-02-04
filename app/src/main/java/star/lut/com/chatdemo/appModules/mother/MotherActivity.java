package star.lut.com.chatdemo.appModules.mother;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindView;
import star.lut.com.chatdemo.Base.BaseActivity;
import star.lut.com.chatdemo.R;
import star.lut.com.chatdemo.appModules.chatThreads.view.MessageThreadFragment;
import star.lut.com.chatdemo.appModules.sendMessage.view.SendMessageFragment;
import star.lut.com.chatdemo.db.PreferenceManager;

public class MotherActivity extends BaseActivity implements SendMessageFragment.SendMessage {
    @BindView(R.id.fragmentContainer) public ConstraintLayout fragmentContainer;
    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.toolbarText) public TextView toolBartext;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        }

        setTitle("");

        toolBartext.setText(new PreferenceManager(this).getUserName());

        fragmentManager = getSupportFragmentManager();
        fragment = new MessageThreadFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_chat_list_activty;
    }

    @Override
    public void onSend() {
        fragment = new MessageThreadFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    @Override
    public void onExist(String senderId, String receiverId) {
        fragment = new MessageThreadFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed(){
        if (fragment instanceof SendMessageFragment){
            fragment = new MessageThreadFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, fragment);
            transaction.commit();
        }else {
            super.onBackPressed();
        }
    }

    public void sendmessage(){
        fragmentManager = getSupportFragmentManager();
        fragment = new SendMessageFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}
