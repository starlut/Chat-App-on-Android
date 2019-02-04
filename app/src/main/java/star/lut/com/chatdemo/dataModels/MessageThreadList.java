package star.lut.com.chatdemo.dataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kamrulhasan on 30/10/18.
 */
public class MessageThreadList {
    @SerializedName("thread_id")
    @Expose
    public String threadId;
    @SerializedName("other_id")
    @Expose
    public String otherId;
    @SerializedName("last_message")
    @Expose
    public String lastMessage;
    @SerializedName("last_message_time")
    @Expose
    public String lastMessageTime;

    @Override
    public String toString() {
        return "MessageThreadList{" +
                "threadId='" + threadId + '\'' +
                ", otherId='" + otherId + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", lastMessageTime='" + lastMessageTime + '\'' +
                '}';
    }
}
