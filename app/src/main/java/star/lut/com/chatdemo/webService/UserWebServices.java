package star.lut.com.chatdemo.webService;

import com.google.gson.JsonArray;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import star.lut.com.chatdemo.dataModels.receiveModel.AllUserResponse;
import star.lut.com.chatdemo.dataModels.sendModel.FirebaseTokenSendModel;
import star.lut.com.chatdemo.dataModels.sendModel.LogoutSendModel;
import star.lut.com.chatdemo.dataModels.sendModel.UpdateProfile;

public interface UserWebServices {


    @GET("get_all_user_info.php")
    Observable<Response<AllUserResponse>> getAllUser(
            @Query("api") String loginSendModel,
            @Query("uid") String userId
    );

    @POST("update_profile.php")
    Observable<Response<JsonArray>> updateProfile(
            @Body UpdateProfile updateProfile
            );

    @POST("firebase_token_store.php")
    Observable<Response<Object>> storeToken(
            @Body FirebaseTokenSendModel model
            );

    @POST("logout.php")
    Observable<Response<Object>> logout(
            @Body LogoutSendModel logoutSendModel
            );

}
