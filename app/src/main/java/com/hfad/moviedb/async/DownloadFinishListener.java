package com.hfad.moviedb.async;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public interface DownloadFinishListener {
    void notify(Bitmap bitmap);
}
