package star.lut.com.chatdemo.dataModels.sendModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kamrulhasan on 20/11/18.
 */
public class LogoutSendModel {
    @SerializedName("user_id")
    @Expose
    public String userId;
}
