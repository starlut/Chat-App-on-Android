package star.lut.com.chatdemo.dataModels.sendModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kamrulhasan on 28/10/18.
 */
public class LoginSendModel {
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("password")
    @Expose
    public String password;

    @Override
    public String toString() {
        return "LoginSendModel{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
