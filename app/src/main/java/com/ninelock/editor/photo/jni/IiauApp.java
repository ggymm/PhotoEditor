package com.ninelock.editor.photo.jni;

public class IiauApp {

    static {
        System.loadLibrary("lib");
    }

    public native String stringFromJNI();
}
