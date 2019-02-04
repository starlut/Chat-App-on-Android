package star.lut.com.chatdemo.dataModels.receiveModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import star.lut.com.chatdemo.dataModels.UnreadMessage;

/**
 * Created by kamrulhasan on 31/10/18.
 */
public class UnreadMessageList {
    @SerializedName("unread_message")
    @Expose
    public List<UnreadMessage> unreadMessage;

    @Override
    public String toString() {
        return "UnreadMessageList{" +
                "unreadMessage=" + unreadMessage +
                '}';
    }
}
