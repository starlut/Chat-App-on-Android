package star.lut.com.chatdemo.appModules.chatDetails.adapter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import star.lut.com.chatdemo.constants.ApiConstants;
import star.lut.com.chatdemo.constants.ValueConstants;
import star.lut.com.chatdemo.appModules.fullScreenPicture.PictureActivty;
import star.lut.com.chatdemo.R;
import star.lut.com.chatdemo.dataModels.MessageDetail;
import star.lut.com.chatdemo.webService.FileDownloadService;
import star.lut.com.chatdemo.webService.WebServiceFactory;
import timber.log.Timber;

/**
 * Created by kamrulhasan on 3/10/18.
 */
public class ChatDetailsRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private int VIEWTYPE_PICTURE = 11;
    private int VIEWTYPE_FILE = 12;
    private int VIEWTYPE_MESSAGE = 13;
    private Context context;
    private List<MessageDetail> chatDetailsModels;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager notificationManager;
    private String ownPic;
    private String otherPic;

    public ChatDetailsRVAdapter(Context context, List<MessageDetail> chatDetailsModels, String ownPic, String otherPic) {
        this.context = context;
        this.chatDetailsModels = chatDetailsModels;
        this.ownPic = ownPic;
        this.otherPic = otherPic;
    }

    class ViewHolderMessage extends RecyclerView.ViewHolder {
        @BindView(R.id.tvMessage) public TextView tvMessage;
        @BindView(R.id.tvTime) public TextView tvTime;
        @BindView(R.id.ivAvatar) public ImageView ivAvatar;
        @BindView(R.id.clChatLayout) public ConstraintLayout clChatLayout;

        private ViewHolderMessage(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ViewHolderPicture extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTime) public TextView tvTime;
        @BindView(R.id.ivAvatar) public ImageView ivAvatar;
        @BindView(R.id.ivPicture) public ImageView ivPicture;
        @BindView(R.id.btnDownload) public ImageButton btnDownload;
        @BindView(R.id.clChatLayout) public ConstraintLayout clChatLayout;

        private ViewHolderPicture(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ViewHolderFile extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTime) public TextView tvTime;
        @BindView(R.id.ivAvatar) public ImageView ivAvatar;
        @BindView(R.id.tvFileTitle) public TextView tvFileTitle;
        @BindView(R.id.btnDownload) public ImageButton btnDownload;
        @BindView(R.id.clChatLayout) public ConstraintLayout clChatLayout;

        private ViewHolderFile(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEWTYPE_FILE){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.rv_chat_file, viewGroup, false
            );

            viewHolder = new ViewHolderFile(v);
        }else if (viewType == VIEWTYPE_PICTURE){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.rv_chat_picture, viewGroup, false
            );

            viewHolder = new ViewHolderPicture(v);
        }else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.rv_chat_message, viewGroup, false
            );

            viewHolder = new ViewHolderMessage(view);
        }
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Timber.d(viewHolder.getClass().toString());
        if (viewHolder instanceof ViewHolderMessage){
            bindMessageViewHolder((ViewHolderMessage) viewHolder, position);
        }else if (viewHolder instanceof ViewHolderPicture){
            bindPictureViewHolder((ViewHolderPicture) viewHolder, position);
        }else if (viewHolder instanceof ViewHolderFile){
            bindFileViewHolder((ViewHolderFile) viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return chatDetailsModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (chatDetailsModels.get(position).contentType) {
            case ValueConstants.CONTENT_TYPE_FILE:
                return VIEWTYPE_FILE;
            case ValueConstants.CONTENT_TYPE_PICTURE:
                return VIEWTYPE_PICTURE;
            default:
                return VIEWTYPE_MESSAGE;
        }
    }

    private void setAvatar(ImageView ivAvatar, String pic){
        Glide.with(context)
                .load(ApiConstants.BASE_URL+pic)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        assert e != null;
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ivAvatar.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(ivAvatar);
    }

    private void bindMessageViewHolder(final ViewHolderMessage viewHolderMessage, int position){
        if (chatDetailsModels.get(position).own){
            viewHolderMessage.clChatLayout.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
        }else {
            viewHolderMessage.clChatLayout.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_dark));
        }
        TextAdapter textAdapter = new TextAdapter(chatDetailsModels.get(position).content);
        SpannableString spannableString = textAdapter.getModifiedText();

        viewHolderMessage.tvMessage.setText(spannableString);
        viewHolderMessage.tvTime.setText(chatDetailsModels.get(position).time);

        if (chatDetailsModels.get(position).own){
            setAvatar(viewHolderMessage.ivAvatar, ownPic);
        }else {
            setAvatar(viewHolderMessage.ivAvatar, otherPic);
        }
    }

    private void bindFileViewHolder(final ViewHolderFile viewHolderFile, final int position){
        if (chatDetailsModels.get(position).own){
            viewHolderFile.clChatLayout.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
        }else {
            viewHolderFile.clChatLayout.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_dark));
        }
        viewHolderFile.tvFileTitle.setText(chatDetailsModels.get(position).title);
        viewHolderFile.tvTime.setText(chatDetailsModels.get(position).time);

        if (chatDetailsModels.get(position).own){
            setAvatar(viewHolderFile.ivAvatar, ownPic);
        }else {
            setAvatar(viewHolderFile.ivAvatar, otherPic);
        }


        viewHolderFile.btnDownload.setOnClickListener(v -> downloadFile(
                ApiConstants.BASE_URL+chatDetailsModels.get(position).content, chatDetailsModels.get(position).title, position, viewHolderFile)
        );
    }

    private void bindPictureViewHolder(final ViewHolderPicture viewHolderPicture, final int position){
        if (chatDetailsModels.get(position).own){
            viewHolderPicture.clChatLayout.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
        }else {
            viewHolderPicture.clChatLayout.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_dark));
        }
        viewHolderPicture.tvTime.setText(chatDetailsModels.get(position).time);


        if (chatDetailsModels.get(position).own){
            setAvatar(viewHolderPicture.ivAvatar, ownPic);
        }else {
            setAvatar(viewHolderPicture.ivAvatar, otherPic);
        }


        viewHolderPicture.btnDownload.setOnClickListener(v -> {
            Timber.d("download clicked");
            downloadFile(ApiConstants.BASE_URL+chatDetailsModels.get(position).content, chatDetailsModels.get(position).title, position, viewHolderPicture);
        });

        viewHolderPicture.ivPicture.setOnClickListener(v -> {
            Intent intent = new Intent(context, PictureActivty.class);
            intent.putExtra("path", chatDetailsModels.get(position).content);
            context.startActivity(intent);
        });

        setAvatar(viewHolderPicture.ivPicture, chatDetailsModels.get(position).content);
    }

    private void downloadFile(String url, final String fileName, final int position, RecyclerView.ViewHolder viewHolder){
        String dirPath = getDirPath();
        PRDownloader.initialize(context.getApplicationContext());

        setNotification(chatDetailsModels.get(position).id, fileName);


        PRDownloader.download(url, dirPath, fileName)
                .build()
                .setOnStartOrResumeListener(() -> setNotification(chatDetailsModels.get(position).id, fileName))
                .setOnPauseListener(() -> {
                    mBuilder.setSubText("Paused");
                    notificationManager.notify(chatDetailsModels.get(position).id, mBuilder.build());
                })
                .setOnCancelListener(() -> {

                })
                .setOnProgressListener(progress -> {
                    mBuilder.setProgress(100, (int) ((double) progress.currentBytes * 100 / progress.totalBytes), false);
                    notificationManager.notify(chatDetailsModels.get(position).id, mBuilder.build());
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Timber.d("download complete");
                        mBuilder.setProgress(0,0,false);
                        mBuilder.setSubText("Download completed");
                        mBuilder.setOngoing(false);
                        notificationManager.notify(chatDetailsModels.get(position).id, mBuilder.build());

                        if (viewHolder instanceof ViewHolderPicture){
                            ((ViewHolderPicture)viewHolder).btnDownload.setImageResource(R.drawable.ic_open);
                            ((ViewHolderPicture)viewHolder).btnDownload.setOnClickListener(view -> {
                                File file = new File(dirPath+fileName);
                                Uri path = Uri.fromFile(file);
                                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                                pdfOpenintent.setDataAndType(path, "application/*");
                                if(Build.VERSION.SDK_INT>=24){
                                    try{
                                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                        m.invoke(null);
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                                try {
                                    context.startActivity(pdfOpenintent);
                                }
                                catch (ActivityNotFoundException e) {
                                    e.printStackTrace();
                                }
                            });
                        }else {
                            ((ViewHolderFile)viewHolder).btnDownload.setImageResource(R.drawable.ic_open);
                            ((ViewHolderFile)viewHolder).btnDownload.setOnClickListener(view -> {
                                File file = new File(dirPath+fileName);
                                Uri path = Uri.fromFile(file);
                                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                                pdfOpenintent.setDataAndType(path, "application/*");
                                if(Build.VERSION.SDK_INT>=24){
                                    try{
                                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                        m.invoke(null);
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                                try {
                                    context.startActivity(pdfOpenintent);
                                }
                                catch (ActivityNotFoundException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Error error) {
                        Timber.d(error.isConnectionError()+" "+error.isServerError());
                        mBuilder.setProgress(0,0,false);
                        mBuilder.setSubText("Download canceled");
                        notificationManager.notify(chatDetailsModels.get(position).id, mBuilder.build());
                    }
                });
    }

    private void setNotification(int notificationid, String fileName){
        Intent intent = new Intent();
        final PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, 0);
        mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_apk_box)
                .setContentTitle(fileName);

        mBuilder.setContentIntent(pendingIntent);
        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.setProgress(100
                , 0, false);
        mBuilder.setOngoing(true);

        notificationManager.notify(notificationid, mBuilder.build());
    }

    private static String getDirPath() {
        Timber.d( "called here");
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "chatDemoDownloads");
        if (!file.exists()) {
            file.mkdirs();
            Timber.d( file.getAbsolutePath());
        } else {
            Timber.d( file.getAbsolutePath());
        }

        return file.getAbsolutePath();
    }

    private void downloadFile(MessageDetail messageDetail, ViewHolderFile file){
        FileDownloadService downloadService = WebServiceFactory.createRetrofitService(FileDownloadService.class);

        downloadService.getFileFromServer(ApiConstants.BASE_URL+ messageDetail.content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        boolean writtenToDisk = writeResponseBodyToDisk(responseBody, messageDetail);
                        if (writtenToDisk){
                            Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show();
                            file.btnDownload.setImageResource(R.drawable.ic_open);
                            file.btnDownload.setOnClickListener(view -> {
                                File file = new File(getDirPath()+messageDetail.title);
                                Uri path = Uri.fromFile(file);
                                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                                pdfOpenintent.setDataAndType(path, "application/*");
                                if(Build.VERSION.SDK_INT>=24){
                                    try{
                                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                        m.invoke(null);
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                                try {
                                    context.startActivity(pdfOpenintent);
                                }
                                catch (ActivityNotFoundException e) {
                                    e.printStackTrace();
                                }
                            });
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

    private void downloadPicture(MessageDetail messageDetail, ViewHolderPicture file){
        FileDownloadService downloadService = WebServiceFactory.createRetrofitService(FileDownloadService.class);

        downloadService.getFileFromServer(ApiConstants.BASE_URL+ messageDetail.content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        boolean writtenToDisk = writeResponseBodyToDisk(responseBody, messageDetail);
                        if (writtenToDisk){
                            Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show();
                            file.btnDownload.setImageResource(R.drawable.ic_open);
                            file.btnDownload.setOnClickListener(view -> {
                                File file = new File(getDirPath()+messageDetail.title);
                                Uri path = Uri.fromFile(file);
                                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                                pdfOpenintent.setDataAndType(path, "application/*");
                                if(Build.VERSION.SDK_INT>=24){
                                    try{
                                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                        m.invoke(null);
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                                try {
                                    context.startActivity(pdfOpenintent);
                                }
                                catch (ActivityNotFoundException e) {
                                    e.printStackTrace();
                                }
                            });
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

    private boolean writeResponseBodyToDisk(ResponseBody body, MessageDetail messageDetail) {
        try {

            File futureStudioIconFile = new File(getDirPath() + File.separator + messageDetail.title);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
