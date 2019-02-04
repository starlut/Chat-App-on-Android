package star.lut.com.chatdemo.appModules.chatDetails.view;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gun0912.tedbottompicker.TedBottomPicker;
import star.lut.com.chatdemo.Base.BaseActivity;
import star.lut.com.chatdemo.Base.BaseApplication;
import star.lut.com.chatdemo.R;
import star.lut.com.chatdemo.appModules.chatDetails.presenter.ChatDetailsPresenter;
import star.lut.com.chatdemo.appModules.chatDetails.presenter.ChatDetailsViewInterface;
import star.lut.com.chatdemo.appModules.chatDetails.presenter.NewMessageReceived;
import star.lut.com.chatdemo.constants.ValueConstants;
import star.lut.com.chatdemo.appModules.chatDetails.adapter.ChatDetailsRVAdapter;
import star.lut.com.chatdemo.dataModels.MessageDetail;
import star.lut.com.chatdemo.dataModels.sendModel.NewMessage;
import star.lut.com.chatdemo.db.PreferenceManager;
import star.lut.com.chatdemo.utils.ProgressBarHandler;
import timber.log.Timber;

public class ChatDetailsActivity extends BaseActivity implements ChatDetailsViewInterface{
    @BindView(R.id.rvChatDetailsList)
    public RecyclerView rvChatDetailsList;
    @BindView(R.id.etChatMessage)
    public EditText etChatMessage;
    @BindView(R.id.clFIleSelector)
    public ConstraintLayout clFileSelector;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.toolbarText)
    public TextView toolBartext;

    private List<MessageDetail> chatDetailsModels;
    private ChatDetailsRVAdapter adapter;
    private ChatDetailsPresenter presenter;
    private ProgressBarHandler handler;
    String senderid, receiverId, fullName, ownPic, otherPic, threadId;

    private CountDownTimer countDownTimer;
    private BroadcastReceiver receiver;

    private PreferenceManager preferenceManager ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        }
        setTitle("");

        preferenceManager = new PreferenceManager(this);
        preferenceManager.setThreadID(threadId);



        senderid = new PreferenceManager(this).getUSerID();

        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Timber.d("asdfasdfbroadcasted");
                presenter.getLastMessage(threadId, senderid);
            }
        };

        countDownTimer = new CountDownTimer(10000 , 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                presenter.getLastMessage(threadId, senderid);
            }
        };

        ownPic = new PreferenceManager(this).getUserPic();

        initRv();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            receiverId = extras.getString(ValueConstants.USER_OTHER_ID);
            fullName = extras.getString(ValueConstants.USER_FULL_NAME);
            otherPic = extras.getString(ValueConstants.USER_OTHER_PIC);
            threadId = extras.getString(ValueConstants.MESSAGE_THREAD_ID);
        }

        toolBartext.setText(fullName);

        handler = new ProgressBarHandler(this);

        presenter = new ChatDetailsPresenter(this, this);
        presenter.getAllMessage(threadId, senderid);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_chat_details;
    }


    private void initRv() {
        rvChatDetailsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        rvChatDetailsList.setLayoutManager(linearLayoutManager);

        chatDetailsModels = new ArrayList<>();
        initAdapter();
    }

    private void initAdapter() {
        adapter = null;
        adapter = new ChatDetailsRVAdapter(this, chatDetailsModels, ownPic, otherPic);
        rvChatDetailsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.ivSend)
    public void sendMessage() {
        if (etChatMessage.getText().toString().trim().length() != 0) {
            sendMessage(etChatMessage.getText().toString().trim(), ValueConstants.CONTENT_TYPE_MESSAGE, " ");
        }
    }

    private void sendMessage(String content, String contentType, String title) {
        NewMessage message = new NewMessage();
        message.content = content;
        message.contentType = contentType;
        message.title = title;
        message.senderId = senderid;
        message.receiverId = receiverId;
        message.threadId = threadId;

        presenter.sendMessage(message);
    }

    @OnClick(R.id.ivAdd)
    public void fileSelector() {
        if (checkPermission()) {
            if (clFileSelector.getVisibility() == View.GONE) {
                clFileSelector.setVisibility(View.VISIBLE);
            } else {
                clFileSelector.setVisibility(View.GONE);
            }
        } else {
            permissionRequest();
        }
    }

    @OnClick(R.id.tvFile)
    public void addFile() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(12345)
                .withHiddenFiles(false)
                .withTitle("Choose File to send")
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12345 && resultCode == RESULT_OK) {
            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            if (path != null) {
                Log.d("chatDetailsActivityS", path);
                File file = new File(path);
                String filename = System.currentTimeMillis()+"_"+ new PreferenceManager(this).getUSerID()+ "_" + path.substring(path.lastIndexOf('.'));

                NewMessage model = new NewMessage();
                model.contentType = ValueConstants.CONTENT_TYPE_FILE;
                model.title = filename;
                model.senderId = senderid;
                model.receiverId = receiverId;
                model.threadId = threadId;

                presenter.uploadFile(file, filename, model);

                closeFileSelector();
            }
        }
    }

    @OnClick(R.id.tvPicture)
    public void addPicture() {
        TedBottomPicker bottomPicker = new TedBottomPicker.Builder(this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        String path = uri.getPath();
                        sendPictureMessage(path);
                    }
                })
                .create();
        bottomPicker.show(getSupportFragmentManager());
    }

    private void sendPictureMessage(String path) {
        File file = new File(path);
        String filename = System.currentTimeMillis()+"_"+ new PreferenceManager(this).getUSerID()+ "_" + path.substring(path.lastIndexOf('.'));

        NewMessage model = new NewMessage();
        model.contentType = ValueConstants.CONTENT_TYPE_PICTURE;
        model.title = filename;
        model.senderId = senderid;
        model.receiverId = receiverId;
        model.threadId = threadId;

        presenter.uploadFile(file, filename, model);

        closeFileSelector();
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
                    fileSelector();
                } else {

                }
                return;
            }
        }
    }

    @OnClick(R.id.ivCloseFileSelector)
    public void closeFileSelector() {
        clFileSelector.setVisibility(View.GONE);
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
    }


    @Override
    public void onMessageListFetched(List<MessageDetail> messageDetails) {
        this.chatDetailsModels = messageDetails;
        Timber.d(messageDetails.toString());
        initAdapter();
    }

    @Override
    public void onNewMessageListFetched(List<MessageDetail> messageDetails) {
        chatDetailsModels.addAll(0, messageDetails);
        adapter.notifyItemInserted(0);
        adapter.notifyDataSetChanged();
        rvChatDetailsList.scrollToPosition(0);
        etChatMessage.setText("");
    }

    @Override
    public void onMessageSent(MessageDetail messageDetail) {
        chatDetailsModels.add(0, messageDetail);
        adapter.notifyItemInserted(0);
        adapter.notifyDataSetChanged();
        rvChatDetailsList.scrollToPosition(0);
        etChatMessage.setText("");
    }

    @Override
    public void onFileUploadSuccess(NewMessage model) {
        presenter.sendMessage(model);
    }

    @Override
    public void onFileUploadFailed() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onNoNewmessage() {
        countDownTimer.start();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        preferenceManager.setThreadID("-1");
    }

    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
        preferenceManager.setThreadID("-1");
//        this.unregisterReceiver(this.receiver);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.d("onResume onResume onResume");
        presenter.getLastMessage(threadId, senderid);

        preferenceManager.setThreadID(threadId);

        try {
            LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                    new IntentFilter("asdf")
            );
        }catch (Exception e){
            e.printStackTrace();
            Timber.d("asdfasdf registered already");
        }


    }

    private void startGettingLastMessage(){
//        countDownTimer.start();
//        presenter.getLastMessage(threadId, senderid);
    }
}
