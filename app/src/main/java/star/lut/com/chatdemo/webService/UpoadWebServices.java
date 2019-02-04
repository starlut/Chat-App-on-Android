package star.lut.com.chatdemo.webService;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kamrulhasan on 14/11/18.
 */
public interface UpoadWebServices {
    @Multipart
    @POST("upload_file.php")
    Observable<Response<JsonObject>> uploadFile(
            @Part("filename")RequestBody filename,
            @Part MultipartBody.Part file
            );
}
