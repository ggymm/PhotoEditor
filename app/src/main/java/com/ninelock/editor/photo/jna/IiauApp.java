package com.ninelock.editor.photo.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface IiauApp extends Library {
    IiauApp Instance =
            (IiauApp)
                    Native.synchronizedLibrary(
                            (IiauApp)
                                    Native.loadLibrary("iiauapp", IiauApp.class));

    String _ZN7IIAUAPP10INPAINTINGC1ESs(String srcPath, String maskPath);

    String _ZN7IIAUAPP10INPAINTING7recoverESsSsSs(String var1, String var2, String var3);

    String Java_com_dlut_test_MainActivity_stringFromJNI();
}
