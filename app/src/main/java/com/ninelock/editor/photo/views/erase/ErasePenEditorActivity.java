package com.ninelock.editor.photo.views.erase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.ninelock.editor.photo.R;
import com.ninelock.editor.photo.views.base.BaseActivity;

import me.minetsh.imaging.core.file.IMGDecoder;
import me.minetsh.imaging.core.file.IMGFileDecoder;
import me.minetsh.imaging.core.util.IMGUtils;
import me.minetsh.imaging.view.IMGView;

public class ErasePenEditorActivity extends BaseActivity {

    private static final String FILEPATH = "FILEPATH";

    public static void goEditor(Context context, String filepath) {
        Intent intent = new Intent(context, ErasePenEditorActivity.class);

        intent.putExtra(FILEPATH, filepath);

        context.startActivity(intent);
    }

    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 1024;
    private String mFilepath;

    protected IMGView imageCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_erase_pen);

        mFilepath = getIntent().getStringExtra(FILEPATH);
        initView();
    }

    public Bitmap getBitmap() {
        IMGDecoder decoder = new IMGFileDecoder(mFilepath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inJustDecodeBounds = true;
        decoder.decode(options);
        if (options.outWidth > MAX_WIDTH) {
            options.inSampleSize = IMGUtils.inSampleSize(Math.round(1F * options.outWidth / MAX_WIDTH));
        }
        if (options.outHeight > MAX_HEIGHT) {
            options.inSampleSize = Math.max(options.inSampleSize, IMGUtils.inSampleSize(Math.round(1F * options.outHeight / MAX_HEIGHT)));
        }
        options.inJustDecodeBounds = false;

        return decoder.decode(options);
    }

    private void initView() {
        Bitmap bitmap = getBitmap();
        if (bitmap != null) {
            imageCanvas = findViewById(R.id.imageCanvas);
            imageCanvas.setImageBitmap(bitmap);
        }
    }


    private void initEvent() {

    }
}
