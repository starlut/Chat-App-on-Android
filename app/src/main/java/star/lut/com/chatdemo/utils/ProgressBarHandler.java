package star.lut.com.chatdemo.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import star.lut.com.chatdemo.R;

/**
 * Created by kamrulhasan on 29/10/18.
 */
public class ProgressBarHandler {

    //private ProgressBar mProgressBar;
    //private Activity activity;
    private Dialog dialog;

    public ProgressBarHandler(Context context) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.custom_progress_dialog);

        //...initialize the imageView form infalted layout
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);


        //...now load that gif which we put inside the drawble folder here with the help of Glide

        Glide.with(context)
                .load(R.drawable.loader)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        gifImageView.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(gifImageView);


    }

    public void showProgress() {
        try {
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void hideProgress() {
        dialog.dismiss();
    }
}
