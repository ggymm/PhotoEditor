package com.ninelock.editor.photo.utils;

public class ImageUtils {

    public static String getFileExt(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf(".");
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;

    }
}
