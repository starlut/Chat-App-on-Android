package star.lut.com.chatdemo.appModules.sendMessage.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import star.lut.com.chatdemo.Base.BaseFragment;
import star.lut.com.chatdemo.R;
import star.lut.com.chatdemo.appModules.sendMessage.autoCompleteAdapter.AutoCompleteReceiverAdapter;
import star.lut.com.chatdemo.appModules.sendMessage.presenter.SendMessagePresenter;
import star.lut.com.chatdemo.appModules.sendMessage.presenter.SendMessageViewInterface;
import star.lut.com.chatdemo.dataModels.UserInfo;
import star.lut.com.chatdemo.dataModels.sendModel.CreateNewMessageSendModel;
import star.lut.com.chatdemo.db.PreferenceManager;
import star.lut.com.chatdemo.utils.ProgressBarHandler;

/**
 * Created by kamrulhasan on 16/10/18.
 */
public class SendMessageFragment extends BaseFragment implements SendMessageViewInterface {
    private static final String ARG_TITLE = "title";
    private String title;

    @BindView(R.id.etReceiverName)
    AutoCompleteTextView etReceiverName;
    @BindView(R.id.etMessageBody)
    EditText etMessagebody;
    private SendMessage sendMessage;

    private ProgressBarHandler progressBarHandler;
    private SendMessagePresenter presenter;


    private List<UserInfo> personModels;
    private UserInfo receiver;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_send_message;
    }

    public SendMessageFragment() {
        //Required empty public constructor
    }

    public static SendMessageFragment newInstance(String title) {
        SendMessageFragment agendaFragment = new SendMessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        agendaFragment.setArguments(args);
        return agendaFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBarHandler = new ProgressBarHandler(getContext());
        presenter = new SendMessagePresenter(getContext(), this);

        presenter.getAllUserList();
    }



    @OnClick(R.id.btnSend)
    public void sendData(){
        if (etMessagebody.getText().toString().trim().length() > 0 && etReceiverName.getText().toString().trim().length() > 0) {
            CreateNewMessageSendModel model = new CreateNewMessageSendModel();
            model.senderId = new PreferenceManager(getContext()).getUSerID();
            model.receiverId = receiver.uid;
            model.message = etMessagebody.getText().toString().trim();

            presenter.createNewMessage(model);
        }
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        sendMessage = (SendMessage) context;
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

    @Override
    public void onUserListFetched(List<UserInfo> userInfos) {
        personModels = userInfos;

        AutoCompleteReceiverAdapter adapter = new AutoCompleteReceiverAdapter(getActivity(), personModels, new AutoCompleteReceiverAdapter.OnClickListerner() {
            @Override
            public void onClick(UserInfo model) {
                etReceiverName.setText(model.fullName);
                receiver = model;
            }
        });
        etReceiverName.setAdapter(adapter);
    }

    @Override
    public void onMessageSendSuccessFull() {
        Toast.makeText(getContext(), "Message sent successfully" , Toast.LENGTH_SHORT).show();
        sendMessage.onSend();
    }

    @Override
    public void onMessageExist(String senderId, String receiverId) {
        Toast.makeText(getContext(), "Message thread already Exists" , Toast.LENGTH_SHORT).show();
        sendMessage.onExist(senderId, receiverId);
    }

    public interface SendMessage{
        void onSend();
        void onExist(String senderId, String receiverId);
    }


}

