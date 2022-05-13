package com.ninelock.editor.photo.config;

import android.content.Context;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.PathUtils;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class GlobalGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        int diskCacheSize = 1024 * 1024 * 400;
        builder.setDiskCache(new DiskLruCacheFactory(PathUtils.getExternalAppCachePath(), "glide", diskCacheSize));
    }
}
