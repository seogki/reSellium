package com.dev.skh.resellium.Base;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dev.skh.resellium.R;
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


    @BindingAdapter("platformCheck")
    public static void platformcheck(final TextView textView, final String result) {
        Context context = textView.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }


        if(result != null) {
            switch (result) {
                case "XBOX":
                    textView.setBackground(ContextCompat.getDrawable(context, R.drawable.text_xbox));
                    break;
                case "PS":
                    textView.setBackground(ContextCompat.getDrawable(context, R.drawable.text_ps4));
                    break;
                default:
                    textView.setBackground(ContextCompat.getDrawable(context, R.drawable.text_switch));
                    break;
            }
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
}
