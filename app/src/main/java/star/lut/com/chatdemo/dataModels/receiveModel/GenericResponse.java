package star.lut.com.chatdemo.dataModels.receiveModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kamrulhasan on 29/10/18.
 */
public class GenericResponse {
    @SerializedName("status")
    @Expose
    public boolean status;
}
