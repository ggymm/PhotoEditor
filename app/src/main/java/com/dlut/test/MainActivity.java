package com.dlut.test;

public class MainActivity {
    static {
        System.loadLibrary("iiauapp");
    }

    public native String stringFromJNI();
}
