package star.lut.com.chatdemo.webService;

import com.google.gson.JsonArray;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import star.lut.com.chatdemo.dataModels.receiveModel.AllMessageResponse;
import star.lut.com.chatdemo.dataModels.receiveModel.MessageThread;
import star.lut.com.chatdemo.dataModels.sendModel.CreateNewMessageSendModel;
import star.lut.com.chatdemo.dataModels.sendModel.NewMessage;

/**
 * Created by kamrulhasan on 28/10/18.
 */
public interface ChatWebServices {
    //create new message
    @POST("create_new_message_thread.php")
    Observable<Response<JsonArray>> createNewMessage(
            @Body CreateNewMessageSendModel model
    );

    @POST("send_message_v2.php")
    Observable<ResponseBody> sendMessage(
            @Body NewMessage newMessage
            );


    @GET("get_all_message_thread.php")
    Observable<Response<MessageThread>> getAllMessageThread(
            @Query("uid") String userId
    );

    @Headers("Cache-control: no-cache")
    @GET("get_all_message.php")
    Observable<Response<AllMessageResponse>> getAllMessage(
            @Query("thread_id") String threadID,
            @Query("uid") String uid

    );

    @Headers("Cache-control: no-cache")
    @GET("get_last_message.php")
    Observable<Response<AllMessageResponse>> getLastMessage(
            @Query("thread_id") String threadID,
            @Query("uid") String uid
    );
}
