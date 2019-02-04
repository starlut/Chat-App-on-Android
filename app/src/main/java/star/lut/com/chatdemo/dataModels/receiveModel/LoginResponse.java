package star.lut.com.chatdemo.dataModels.receiveModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kamrulhasan on 28/10/18.
 */
public class LoginResponse {
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("pic")
    @Expose
    public String pic;
    @SerializedName("position")
    @Expose
    public String position;
    @SerializedName("full_name")
    @Expose
    public String fullName;

    @Override
    public String toString() {
        return "LoginResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", pic='" + pic + '\'' +
                ", position='" + position + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
