package star.lut.com.chatdemo.dataModels.sendModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kamrulhasan on 19/11/18.
 */
public class FirebaseTokenSendModel {
    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("user_id")
    @Expose
    public String userid;

    public FirebaseTokenSendModel(String token, String userid) {
        this.token = token;
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "FirebaseTokenSendModel{" +
                "token='" + token + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }
}
