package com.dlut.iiauapp;

public class CommonNative {
    public static String modelRootPath = "assets/";
    static {
        System.loadLibrary("iiauapp");
        System.out.println("iiauapp");
    }

}
