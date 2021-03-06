package me.minetsh.imaging.core.file;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

/**
 * Created by felix on 2017/12/26 下午2:54.
 */

public abstract class IMGDecoder {

    public String getFilepath() {
        return filepath;
    }

    private final String filepath;

    public IMGDecoder(String filepath) {
        this.filepath = filepath;
    }

    public Bitmap decode() {
        return decode(null);
    }

    public abstract Bitmap decode(BitmapFactory.Options options);

}
