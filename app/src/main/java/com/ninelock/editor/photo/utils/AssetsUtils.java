package com.ninelock.editor.photo.utils;

import android.content.Context;

import com.blankj.utilcode.util.PathUtils;
import com.dlut.iiauapp.CommonNative;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssetsUtils {

    public static void copyFileFromAssets(Context context, String filename) {
        String parentPath = PathUtils.getExternalAppDataPath() + File.separator + "mnn" + File.separator;
        File parentFile = new File(parentPath);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String filepath = parentPath + filename;
        try {
            InputStream inputStream = context.getAssets().open(filename);
            File file = new File(filepath);
            if (file.exists()) {
                file.delete();
            }
            if (!file.exists() || file.length() == 0) {
                FileOutputStream outputStream = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.flush();
                inputStream.close();
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        CommonNative.modelRootPath = parentPath;
    }
}
