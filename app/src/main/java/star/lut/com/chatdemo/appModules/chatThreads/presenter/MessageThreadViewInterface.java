package star.lut.com.chatdemo.appModules.chatThreads.presenter;

import java.util.List;

import star.lut.com.chatdemo.Base.MvpView;
import star.lut.com.chatdemo.dataModels.MessageThreadList;
import star.lut.com.chatdemo.dataModels.UserInfo;
import star.lut.com.chatdemo.dataModels.receiveModel.MessageThread;

/**
 * Created by kamrulhasan on 28/10/18.
 */
public interface MessageThreadViewInterface extends MvpView {
    void onNewThreadListRetrieved(List<MessageThreadList> messageThreads);
    void onNewUserDataFetched(List<UserInfo> userInfos);
}
