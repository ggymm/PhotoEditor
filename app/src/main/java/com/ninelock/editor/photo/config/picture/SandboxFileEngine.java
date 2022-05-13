package com.ninelock.editor.photo.config.picture;

import static com.luck.picture.lib.config.PictureMimeType.JPG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import com.blankj.utilcode.util.PathUtils;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.engine.UriToFileTransformEngine;
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;

public class SandboxFileEngine implements UriToFileTransformEngine {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat SF_FOLDER = new SimpleDateFormat("yyyyMMdd");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat SF_FILE = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    @Override
    public void onUriToFileAsyncTransform(Context context, String url, String mineType, OnKeyValueResultCallbackListener call) {
        if (call != null) {
            call.onCallback(url, copyPathToSandbox(context, url, mineType));
        }
    }

    private String getLastImgSuffix(String mineType) {
        try {
            return mineType.substring(mineType.lastIndexOf("/")).replace("/", ".");
        } catch (Exception e) {
            e.printStackTrace();
            return JPG;
        }
    }

    private File createSandboxFile(String mineType) {
        long millis = System.currentTimeMillis();
        String sandboxPath = PathUtils.getExternalAppFilesPath() + "/temp_photo/" + SF_FOLDER.format(millis);

        File sandboxFolder = new File(sandboxPath);
        if (!sandboxFolder.exists()) {
            sandboxFolder.mkdirs();
        }

        final File sandboxFile = new File(sandboxFolder, SF_FILE.format(millis) + getLastImgSuffix(mineType));
        if (sandboxFile.exists()) {
            sandboxFile.delete();
        }
        return sandboxFile;
    }

    private String copyPathToSandbox(Context context, String url, String mineType) {
        try {
            File sandboxFile = createSandboxFile(mineType);
            FileOutputStream outputStream = new FileOutputStream(sandboxFile);

            InputStream inputStream;
            if (PictureMimeType.isContent(url)) {
                inputStream = context.getContentResolver().openInputStream(Uri.parse(url));
            } else {
                inputStream = new FileInputStream(url);
            }

            byte[] b = new byte[1024];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, length);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();

            return sandboxFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
