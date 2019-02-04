package star.lut.com.chatdemo.firebaseServices;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FirebaseNotification {
    @SerializedName("sender_id")
    @Expose
    public String senderId;
    @SerializedName("content_type")
    @Expose
    public String contentType;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("thread_id")
    @Expose
    public String threadId;
    @SerializedName("sender_name")
    @Expose
    public String senderName;
    @SerializedName("profile_picture")
    @Expose
    public String profilePicture;

    @Override
    public String toString() {
        return "FirebaseNotification{" +
                "senderId='" + senderId + '\'' +
                ", senderName='" + senderName + '\'' +
                ", content='" + content + '\'' +
                ", contentType='" + contentType + '\'' +
                ", title='" + title + '\'' +
                ", threadId='" + threadId + '\'' +
                '}';
    }
}
