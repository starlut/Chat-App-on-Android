package star.lut.com.chatdemo.dataModels.sendModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kamrulhasan on 30/10/18.
 */
public class NewMessage {
    @SerializedName("sender_id")
    @Expose
    public String senderId;
    @SerializedName("receiver_id")
    @Expose
    public String receiverId;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("content_type")
    @Expose
    public String contentType;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("thread_id")
    public String threadId;

    @Override
    public String toString() {
        return "NewMessage{" +
                "senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", content='" + content + '\'' +
                ", contentType='" + contentType + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
