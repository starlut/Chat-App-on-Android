package star.lut.com.chatdemo.webService;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import star.lut.com.chatdemo.dataModels.receiveModel.LoginResponse;
import star.lut.com.chatdemo.dataModels.sendModel.LoginSendModel;

/**
 * Created by kamrulhasan on 28/10/18.
 */
public interface LoginWebService {

    @POST("login.php")
    Observable<Response<List<LoginResponse>>> loginUser(
            @Body LoginSendModel loginSendModel
            );
}
