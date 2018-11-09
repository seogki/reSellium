package com.dev.skh.resellium.Base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.TextView;

/**
 * Created by Seogki on 2018. 4. 12..
 */

public class BaseBindingAdapter {

//    @BindingAdapter("galleryImageUrl")
//    public static void galleryImage(final ImageView imageView, String url) {
//
//        Context context = imageView.getContext();
//        if (context == null) {
//            return;
//        } else if (context instanceof Activity) {
//            final Activity activity = (Activity) context;
//            if (activity.isFinishing() || activity.isDestroyed()) {
//                return;
//            }
//        }
//
//        if (url == null) {
//            Glide.with(imageView.getContext()).clear(imageView);
//            imageView.setImageDrawable(null);
//        } else {
//
//            Uri uri = Uri.parse("file://" + url);
//            Glide.with(imageView.getContext())
//                    .load(uri)
//                    .apply(new RequestOptions()
//                            .centerCrop()
//                            .override(175, 175)
//                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
//                    .thumbnail(0.1f)
//                    .into(new SimpleTarget<Drawable>() {
//                        @Override
//                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                            imageView.setImageDrawable(resource);
//                        }
//                    });
//        }
//    }




}
