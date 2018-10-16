package com.dev.skh.resellium.Base;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.dev.skh.resellium.R;
import com.dev.skh.resellium.Util.Const;
import com.dev.skh.resellium.Util.KeyboardUtils;
import com.dev.skh.resellium.Util.UtilMethod;

/**
 * Created by Seogki on 2018. 4. 12..
 */

public class BaseBindingAdapter {

    @BindingAdapter("galleryImageUrl")
    public static void galleryImage(final ImageView imageView, String url) {

        Context context = imageView.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }

        if (url == null) {
            Glide.with(imageView.getContext()).clear(imageView);
            imageView.setImageDrawable(null);
        } else {

            Uri uri = Uri.parse("file://" + url);
            Glide.with(imageView.getContext())
                    .load(uri)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .override(175, 175)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .thumbnail(0.1f)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            imageView.setImageDrawable(resource);
                        }
                    });
        }
    }

    @BindingAdapter("keyboardDetect")
    public static void keyboardDetect(@NonNull final View view, final String data) {

        Context context = view.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }


        if (UtilMethod.getActivity(view.getContext()) != null)
            KeyboardUtils.addKeyboardToggleListener(UtilMethod.getActivity(view.getContext()), new KeyboardUtils.SoftKeyboardToggleListener() {
                @Override
                public void onToggleSoftKeyboard(boolean isVisible) {
                    view.setVisibility(isVisible ? View.GONE : View.VISIBLE);
                }
            });

    }

    @BindingAdapter("popularImage")
    public static void popularImage(final ImageView view, final String result) {
        Context context = view.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }
        if (result == null) {
            Glide.with(context).clear(view);
            view.setImageDrawable(null);
        } else {
            String end = result.replace("/","");
            String murl = Const.Companion.getServer_url() + end;
            Uri uri = Uri.parse(murl);

            Glide.with(context)
                    .asBitmap()
                    .load(uri)
                    .apply(new RequestOptions()
                            .dontTransform()
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            view.setImageBitmap(resource);
                        }
                    });
        }
    }


    @BindingAdapter("sellCheck")
    public static void sellcheck(final TextView textView, final String result) {
        Context context = textView.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }


        if (result.contains("매입")) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else {
            textView.setTextColor(ContextCompat.getColor(context, R.color.Red));
        }

        textView.setText(result);
    }

    @BindingAdapter("moneyCheck")
    public static void moneycheck(final TextView textView, final String result) {
        Context context = textView.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }

        String money = UtilMethod.currencyFormat(result);
        textView.setText(money + "원");

    }

    @BindingAdapter("soldCheck")
    public static void soldCheck(final TextView textView, final String result) {
        Context context = textView.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }
        if(result == null)
            return;

        if(result.contains("매입")){
            textView.setText(result);
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.text_green));
        } else if(result.contains("매각")){
            textView.setText(result);
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.text_red));
        }


    }
}
