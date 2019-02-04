package star.lut.com.chatdemo.dataModels.receiveModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import star.lut.com.chatdemo.dataModels.UserInfo;

public class AllUserResponse {
    @SerializedName("user_info")
    @Expose
    public List<UserInfo> userInfo;

    @Override
    public String toString() {
        return "AllUserResponse{" +
                "userInfo=" + userInfo +
                '}';
    }
}
