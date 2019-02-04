package star.lut.com.chatdemo.appModules.sendMessage.presenter;

import java.util.List;

import star.lut.com.chatdemo.Base.MvpView;
import star.lut.com.chatdemo.dataModels.UserInfo;
import star.lut.com.chatdemo.dataModels.receiveModel.AllUserResponse;

/**
 * Created by kamrulhasan on 29/10/18.
 */
public interface SendMessageViewInterface extends MvpView {
    void onUserListFetched(List<UserInfo> userInfos);

    void onMessageSendSuccessFull();
    void onMessageExist(String senderId, String receiverId);
}
