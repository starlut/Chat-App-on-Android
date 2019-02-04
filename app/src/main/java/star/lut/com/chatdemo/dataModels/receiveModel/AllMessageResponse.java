package star.lut.com.chatdemo.dataModels.receiveModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import star.lut.com.chatdemo.dataModels.MessageDetail;

/**
 * Created by kamrulhasan on 30/10/18.
 */
public class AllMessageResponse {
    @SerializedName("message_thread_list")
    @Expose
    public List<MessageDetail> messageDetails;

    @Override
    public String toString() {
        return "AllMessageResponse{" +
                "messageDetails=" + messageDetails +
                '}';
    }
}
