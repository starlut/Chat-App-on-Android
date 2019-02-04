package star.lut.com.chatdemo.dataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kamrulhasan on 31/10/18.
 */
public class UnreadMessage {
    @SerializedName("message_id")
    @Expose
    public String messageId;
}
