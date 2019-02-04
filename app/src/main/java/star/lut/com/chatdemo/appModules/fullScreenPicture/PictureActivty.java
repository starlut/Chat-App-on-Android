package star.lut.com.chatdemo.appModules.fullScreenPicture;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;

import butterknife.BindView;
import star.lut.com.chatdemo.Base.BaseActivity;
import star.lut.com.chatdemo.R;
import star.lut.com.chatdemo.constants.ApiConstants;

public class PictureActivty extends BaseActivity {
    @BindView(R.id.ivPicture)
    public ImageView imageView;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            path = extras.getString("path");
        }
        Log.d("picturepath", path);


//        Picasso.get()
//                .load(Uri.fromFile(new File(path)))
//                .error(android.R.drawable.ic_menu_gallery)
//                .into(imageView);

        Glide.with(this)
                .load(ApiConstants.BASE_URL+path)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(imageView);

//        Uri.fromFile(new File(path))

    }

    @Override
    public int getLayout() {
        return R.layout.activity_picture_activty;
    }
}
