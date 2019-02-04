package star.lut.com.chatdemo.dataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kamrulhasan on 30/10/18.
 */
public class MessageDetail {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("content_type")
    @Expose
    public String contentType;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("own")
    @Expose
    public Boolean own;
    @SerializedName("time")
    @Expose
    public String time;

    @Override
    public String toString() {
        return "MessageDetail{" +
                "id=" + id +
                ", contentType='" + contentType + '\'' +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", own=" + own +
                ", time='" + time +
                '}';
    }
}
