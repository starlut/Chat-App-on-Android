package star.lut.com.chatdemo.appModules.chatDetails.presenter;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import star.lut.com.chatdemo.dataModels.MessageDetail;
import star.lut.com.chatdemo.dataModels.receiveModel.AllMessageResponse;
import star.lut.com.chatdemo.dataModels.sendModel.NewMessage;
import star.lut.com.chatdemo.webService.ChatWebServices;
import star.lut.com.chatdemo.webService.UpoadWebServices;
import star.lut.com.chatdemo.webService.WebServiceFactory;
import timber.log.Timber;

/**
 * Created by kamrulhasan on 28/10/18.
 */
public class ChatDetailsPresenter {
    private Context context;
    private ChatDetailsViewInterface viewInterface;
    private ChatWebServices webServices;
    private String threadId;

    private List<MessageDetail> messageDetails;

    public ChatDetailsPresenter(Context context, ChatDetailsViewInterface viewInterface) {
        this.context = context;
        this.viewInterface = viewInterface;

        webServices = WebServiceFactory.createRetrofitService(ChatWebServices.class);

    }

    public void sendMessage(NewMessage message){
        viewInterface.showProgress();

        Timber.d(message.toString());

        webServices.sendMessage(message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody response) {
                        Timber.d(response.toString());
                        MessageDetail messageDetail = new MessageDetail();
                        messageDetail.own = true;
                        messageDetail.time = Calendar.getInstance().getTime().toString();
                        messageDetail.contentType = message.contentType;
                        messageDetail.content = message.content;
                        messageDetail.title = message.title;

                        viewInterface.onMessageSent(messageDetail);
                    }

                    @Override
                    public void onError(Throwable e) {
                        viewInterface.hideProgress();
                        Toast.makeText(context, "Cant send message", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.hideProgress();
                    }
                });
    }

    public void getLastMessage(String threadId, String uid) {

        webServices = WebServiceFactory.createRetrofitService(ChatWebServices.class);

        webServices.getLastMessage(threadId, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<AllMessageResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<AllMessageResponse> allMessageResponseResponse) {
                        Timber.d("last message "+allMessageResponseResponse.toString());

                        if (allMessageResponseResponse.isSuccessful() && allMessageResponseResponse.body() != null){
                            viewInterface.onNewMessageListFetched(allMessageResponseResponse.body().messageDetails);
                        }else {
                            viewInterface.onNoNewmessage();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        viewInterface.hideProgress();
                    }
                });
    }

    public void getAllMessage(String threadId, String uid){
        viewInterface.showProgress();
        this.threadId = threadId;

        webServices.getAllMessage(threadId, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<AllMessageResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<AllMessageResponse> allMessageResponseResponse) {
                        if (allMessageResponseResponse.isSuccessful()){
                            if(allMessageResponseResponse.body().messageDetails != null){
                                Timber.d(allMessageResponseResponse.body().toString());
                                messageDetails = allMessageResponseResponse.body().messageDetails;

                                viewInterface.onMessageListFetched(messageDetails);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        viewInterface.hideProgress();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        viewInterface.hideProgress();
                    }
                });
    }

    public void uploadFile(File imageFile, String fileNmae, NewMessage newMessage){
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
                                newMessage.content = jsonArrayResponse.body().get("file_location").getAsString();

                                viewInterface.onFileUploadSuccess(newMessage);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            viewInterface.onFileUploadFailed();
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

}
