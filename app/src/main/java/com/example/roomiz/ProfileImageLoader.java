package com.example.roomiz;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

public final class ProfileImageLoader {
    private ProfileImageLoader() {
    }

    public static void load(ImageView imageView, String imageName) {
        Glide.with(imageView).clear(imageView);
        imageView.setImageDrawable(null);
        if (imageName == null || imageName.isEmpty()) {
            return;
        }

        int imageId = imageView.getContext().getResources().getIdentifier(
                imageName, "drawable", imageView.getContext().getPackageName());
        if (imageId != 0) {
            Glide.with(imageView)
                    .load(imageId)
                    .centerCrop()
                    .into(imageView);
        }
    }
}