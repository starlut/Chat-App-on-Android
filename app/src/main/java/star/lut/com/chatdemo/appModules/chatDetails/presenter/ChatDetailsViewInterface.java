package star.lut.com.chatdemo.appModules.chatDetails.presenter;

import java.util.List;

import star.lut.com.chatdemo.Base.MvpView;
import star.lut.com.chatdemo.dataModels.MessageDetail;
import star.lut.com.chatdemo.dataModels.sendModel.NewMessage;

/**
 * Created by kamrulhasan on 28/10/18.
 */
public interface ChatDetailsViewInterface extends MvpView {
    void onMessageListFetched(List<MessageDetail> messageDetails);
    void onNewMessageListFetched(List<MessageDetail> messageDetails);

    void onNoNewmessage();
    void onMessageSent(MessageDetail messageDetail);

    void onFileUploadSuccess(NewMessage newMessage);
    void onFileUploadFailed();
}
