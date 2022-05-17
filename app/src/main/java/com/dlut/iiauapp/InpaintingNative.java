package com.dlut.iiauapp;

public class InpaintingNative extends CommonNative {
    public InpaintingNative() {
        init(modelRootPath+"inpainting.mnn");
    }

    public InpaintingNative(String modelPath) {
        init(modelPath);
    }

    public native int init(String modelPath);

    public native int inpainting(String img_path, String mask_path, String out_path);

    public native int recover(String src_img_path, String inpainting_img_path, String mask_path, String out_path);

    public native int deinit();
}
