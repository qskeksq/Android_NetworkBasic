package network.kotlin.flow9.net.networkbasic.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

public class ImageUtil {

    public static void SimpleImageLoad(Context context, int drawable, ImageView view) {
        Glide.with(context)
                .load(drawable)
                .error(0)
                .placeholder(0)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void SimpleImageLoad(Context context, File file, ImageView view) {
        Uri imageUri = Uri.fromFile(file);
        Glide.with(context)
                .load(imageUri)
                .error(0)
                .placeholder(0)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(view);
    }

    public static void SimpleImageLoad(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .error(0)
                .placeholder(0)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(view);
    }

    public static void ImageLoadWithBitmapTarget(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .error(0)
                .placeholder(0)
                .into(new BitmapImageViewTarget(view) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                    }
                });
    }

    public static void ImageLoadWithSimpleTarget(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .error(0)
                .placeholder(0)
                .into(new SimpleTarget<Bitmap>(250, 250) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                    }
                });
    }


}
