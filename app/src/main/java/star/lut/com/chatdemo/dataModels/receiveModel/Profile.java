package star.lut.com.chatdemo.dataModels.receiveModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kamrulhasan on 14/11/18.
 */
public class Profile {
    @SerializedName("fullname")
    @Expose
    public String fullname;
    @SerializedName("picture")
    @Expose
    public String picture;
    @SerializedName("position")
    @Expose
    public String position;
}
