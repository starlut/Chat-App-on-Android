package star.lut.com.chatdemo.dataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kamrulhasan on 29/10/18.
 */
public class UserInfo {
    @SerializedName("uid")
    @Expose
    public String uid;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("full_name")
    @Expose
    public String fullName;
    @SerializedName("picture")
    @Expose
    public String picture;
    @SerializedName("position")
    @Expose
    public String position;
    @SerializedName("lastlogin")
    @Expose
    public String lastlogin;

    public UserInfo(String uid, String username, String fullName, String picture, String position, String lastlogin) {
        this.uid = uid;
        this.username = username;
        this.fullName = fullName;
        this.picture = picture;
        this.position = position;
        this.lastlogin = lastlogin;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", picture='" + picture + '\'' +
                ", position='" + position + '\'' +
                ", lastlogin='" + lastlogin + '\'' +
                '}';
    }
}
