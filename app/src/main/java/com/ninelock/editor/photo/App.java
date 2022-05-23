package com.ninelock.editor.photo;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

import com.ninelock.editor.photo.utils.AssetsUtils;

public class App extends Application {

    @Override
    protected void attachBaseContext(@Nullable Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            // 拷贝模型到目录
            AssetsUtils.copyFileFromAssets(this, "inpainting.mnn");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
