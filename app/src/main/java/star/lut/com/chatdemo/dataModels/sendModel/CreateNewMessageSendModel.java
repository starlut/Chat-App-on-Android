package star.lut.com.chatdemo.dataModels.sendModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kamrulhasan on 29/10/18.
 */
public class CreateNewMessageSendModel {
    @SerializedName("sender_id")
    @Expose
    public String senderId;
    @SerializedName("receiver_id")
    @Expose
    public String receiverId;
    @SerializedName("message")
    @Expose
    public String message;
}
