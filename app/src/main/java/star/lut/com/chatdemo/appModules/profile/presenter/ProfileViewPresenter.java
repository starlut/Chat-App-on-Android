package star.lut.com.chatdemo.appModules.profile.presenter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;

import gun0912.tedbottompicker.TedBottomPicker;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import star.lut.com.chatdemo.appModules.profile.view.ProfileActivity;
import star.lut.com.chatdemo.dataModels.sendModel.LogoutSendModel;
import star.lut.com.chatdemo.dataModels.sendModel.NewMessage;
import star.lut.com.chatdemo.dataModels.sendModel.UpdateProfile;
import star.lut.com.chatdemo.db.PreferenceManager;
import star.lut.com.chatdemo.webService.UpoadWebServices;
import star.lut.com.chatdemo.webService.UserWebServices;
import star.lut.com.chatdemo.webService.WebServiceFactory;
import timber.log.Timber;

/**
 * Created by kamrulhasan on 14/11/18.
 */
public class ProfileViewPresenter {
    private Context context;
    private ProfileViewInterface viewInterface;

    public ProfileViewPresenter(Context context, ProfileViewInterface viewInterface) {
        this.context = context;
        this.viewInterface = viewInterface;
    }

    public void updateProfilePicture(String picPath){
        PreferenceManager manager = new PreferenceManager(context);
        UpdateProfile updateProfile = new UpdateProfile(
                Integer.parseInt(manager.getUSerID()),
                manager.getUserName(),
                manager.getFullName(),
                manager.getPassword(),
                picPath,
                manager.getUserPosition()
        );

        UserWebServices webServices = WebServiceFactory.createRetrofitService(UserWebServices.class);

        webServices.updateProfile(updateProfile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<JsonArray>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<JsonArray> jsonArrayResponse) {
                        if (jsonArrayResponse.isSuccessful()){
                            manager.setUserPic(picPath);
                            viewInterface.onProfilePicUpdated();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void selectPicture() {
        TedBottomPicker bottomPicker = new TedBottomPicker.Builder(context)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        String path = uri.getPath();
                        File file = new File(path);
                        uploadFile(file, System.currentTimeMillis()+"_"+ new PreferenceManager(context).getUSerID()+ "_" + path.substring(path.lastIndexOf('.')));
                    }
                })
                .create();
        bottomPicker.show(((ProfileActivity)context).getSupportFragmentManager());
    }

    public void uploadFile(File imageFile, String fileNmae){
        viewInterface.showProgress();

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("*/*"), imageFile);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("fileToUpload", imageFile.getName(), requestFile);

        RequestBody fileName = RequestBody.create(
                MultipartBody.FORM, fileNmae
        );

        Timber.d(fileNmae);

        UpoadWebServices upoadWebServices = WebServiceFactory.createRetrofitService(UpoadWebServices.class);

        upoadWebServices.uploadFile(fileName, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<JsonObject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        viewInterface.showProgress();
                    }

                    @Override
                    public void onNext(Response<JsonObject> jsonArrayResponse) {
                        Timber.d(jsonArrayResponse.toString());
                        if (jsonArrayResponse.isSuccessful()){
                            try {
                                String path = jsonArrayResponse.body().get("file_location").getAsString();

                                updateProfilePicture(path);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            viewInterface.hideProgress();
                            Toast.makeText(context, "Cant upload file now! ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        viewInterface.hideProgress();
                        e.printStackTrace();
                        Toast.makeText(context, "Cant upload file now! "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.hideProgress();
                    }
                });
    }

    public void logout(String userid){
        LogoutSendModel sendModel = new LogoutSendModel();
        sendModel.userId = userid;

        UserWebServices webServices = WebServiceFactory.createRetrofitService(UserWebServices.class);
        webServices.logout(sendModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<Object> response) {
                        viewInterface.onLogout();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
