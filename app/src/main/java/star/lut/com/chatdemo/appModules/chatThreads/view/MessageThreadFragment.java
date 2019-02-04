package star.lut.com.chatdemo.appModules.chatThreads.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import star.lut.com.chatdemo.Base.BaseFragment;
import star.lut.com.chatdemo.R;
import star.lut.com.chatdemo.appModules.chatDetails.view.ChatDetailsActivity;
import star.lut.com.chatdemo.appModules.chatThreads.presenter.MessageThreadPresenter;
import star.lut.com.chatdemo.appModules.chatThreads.presenter.MessageThreadViewInterface;
import star.lut.com.chatdemo.appModules.mother.MotherActivity;
import star.lut.com.chatdemo.appModules.profile.view.ProfileActivity;
import star.lut.com.chatdemo.appModules.settings.SettingsActivity;
import star.lut.com.chatdemo.appModules.chatThreads.chatListAdapter.ChatListRvAdapter;
import star.lut.com.chatdemo.constants.ValueConstants;
import star.lut.com.chatdemo.dataModels.MessageThreadList;
import star.lut.com.chatdemo.dataModels.UserInfo;
import star.lut.com.chatdemo.utils.ProgressBarHandler;
import timber.log.Timber;

/**
 * Created by kamrulhasan on 16/10/18.
 */
public class MessageThreadFragment extends BaseFragment implements MessageThreadViewInterface {
    private static final String ARG_TITLE = "title";
    private String title;
    private Fragment fragment;
    private FragmentManager fragmentManager;

    @BindView(R.id.rvChatList) public RecyclerView rvChatList;
    private List<MessageThreadList> chatThreadModel;

    public List<UserInfo> userInfos;
    private MessageThreadPresenter presenter;
    private ProgressBarHandler barHandler;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat_threads;
    }

    public MessageThreadFragment() {
        //Required empty public constructor
    }

    public static MessageThreadFragment newInstance(String title) {
        MessageThreadFragment agendaFragment = new MessageThreadFragment();
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
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.settings_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            case R.id.menu_profile:
                Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(profileIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new MessageThreadPresenter(getContext(), this);
        barHandler = new ProgressBarHandler(getContext());
        presenter.getAllMessageThread();
    }



    private void initRv(){
        rvChatList.setHasFixedSize(true);
        rvChatList.setLayoutManager(new LinearLayoutManager(getContext()));

        ChatListRvAdapter chatListRvAdapter = new ChatListRvAdapter(getContext(), chatThreadModel, new ChatListRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String otherid, String fullName, String pic, String threadId) {
                Intent intent = new Intent(getContext() , ChatDetailsActivity.class);
                Timber.d("fullname"+fullName);
                intent.putExtra(ValueConstants.USER_OTHER_ID, otherid);
                intent.putExtra(ValueConstants.USER_FULL_NAME, fullName);
                intent.putExtra(ValueConstants.USER_OTHER_PIC, pic);
                intent.putExtra(ValueConstants.MESSAGE_THREAD_ID, threadId);

                startActivity(intent);
            }
        }, userInfos);

        rvChatList.setAdapter(chatListRvAdapter);

        chatListRvAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.fabAdd)
    public void addMessage(){
        ((MotherActivity)getActivity()).sendmessage();
    }

    @Override
    public void onNewThreadListRetrieved(List<MessageThreadList> messageThreads) {
        this.chatThreadModel = messageThreads;

        presenter.getAllUserInfo();
    }

    @Override
    public void onNewUserDataFetched(List<UserInfo> userInfos) {
        this.userInfos = userInfos;
//        if (rvChatList != null) {
            initRv();
//        }
    }

    @Override
    public void showProgress() {
        barHandler.showProgress();
    }

    @Override
    public void hideProgress() {
        barHandler.hideProgress();
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
    }


}
