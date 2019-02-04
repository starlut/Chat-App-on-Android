package star.lut.com.chatdemo.dataModels.sendModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProfile {
    @SerializedName("uid")
    @Expose
    private Integer uid;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_full_name")
    @Expose
    private String userFullName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("position")
    @Expose
    private String position;

    public UpdateProfile(Integer uid, String userName, String userFullName, String password, String profilePicture, String position) {
        this.uid = uid;
        this.userName = userName;
        this.userFullName = userFullName;
        this.password = password;
        this.profilePicture = profilePicture;
        this.position = position;
    }
}
