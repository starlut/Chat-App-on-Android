package star.lut.com.chatdemo.dataModels.receiveModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import star.lut.com.chatdemo.dataModels.MessageThreadList;

/**
 * Created by kamrulhasan on 30/10/18.
 */
public class MessageThread {
    @SerializedName("message_thread_list")
    @Expose
    public List<MessageThreadList> messageThreadLists;

    @Override
    public String toString() {
        return "MessageThread{" +
                "messageThreadLists=" + messageThreadLists +
                '}';
    }
}
