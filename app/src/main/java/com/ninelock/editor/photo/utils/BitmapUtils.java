package com.ninelock.editor.photo.utils;

import android.graphics.Bitmap;

import java.nio.ByteBuffer;

public class BitmapUtils {

    public static byte[] bitmap2RGB(Bitmap bitmap) {
        // 返回可用于储存此位图像素的最小字节数
        int bytes = bitmap.getByteCount();

        // 使用allocate()静态方法创建字节缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        // 将位图的像素复制到指定的缓冲区
        bitmap.copyPixelsToBuffer(buffer);

        byte[] rgba = buffer.array();
        byte[] pixels = new byte[(rgba.length / 4) * 3];

        int count = rgba.length / 4;

        // Bitmap像素点的色彩通道排列顺序是RGBA
        for (int i = 0; i < count; i++) {
            // R
            pixels[i * 3] = rgba[i * 4];
            // G
            pixels[i * 3 + 1] = rgba[i * 4 + 1];
            // B
            pixels[i * 3 + 2] = rgba[i * 4 + 2];
        }
        return pixels;
    }
}
