package com.ninelock.editor.photo.views.erase;

import static com.blankj.utilcode.util.SizeUtils.dp2px;
import static me.minetsh.imaging.core.IMGMode.DOODLE;
import static me.minetsh.imaging.core.IMGMode.NONE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.ninelock.editor.photo.R;
import com.ninelock.editor.photo.views.base.BaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

import me.minetsh.imaging.core.file.IMGDecoder;
import me.minetsh.imaging.core.file.IMGFileDecoder;
import me.minetsh.imaging.core.util.IMGUtils;
import me.minetsh.imaging.view.IMGView;

public class ErasePenEditorActivity extends BaseActivity implements View.OnClickListener {

    private static final String FILEPATH = "FILEPATH";

    public static void goEditor(Context context, String filepath) {
        Intent intent = new Intent(context, ErasePenEditorActivity.class);

        intent.putExtra(FILEPATH, filepath);

        context.startActivity(intent);
    }

    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 1024;
    private String mFilepath;

    private TitleBar mTitleBar;
    private IMGView mImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_erase_pen);

        mFilepath = getIntent().getStringExtra(FILEPATH);

        initView();
        initEvent();
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
        mTitleBar = findViewById(R.id.titleBar);

        Bitmap bitmap = getBitmap();
        if (bitmap != null) {
            mImgView = findViewById(R.id.imgView);
            mImgView.setImageTop(dp2px(48));
            mImgView.setImageBottom(dp2px(68));
            mImgView.setImageBitmap(bitmap);
        }
    }

    private void initEvent() {
        mTitleBar.setOnTitleBarListener(new OnTitleBarListener() {

            @Override
            public void onLeftClick(TitleBar titleBar) {
                finish();
            }

            @Override
            public void onTitleClick(TitleBar titleBar) {
            }

            @Override
            public void onRightClick(TitleBar titleBar) {
                ToastUtils.showLong("点击完成按钮");

                // 生成像素点覆盖集合
                Bitmap bitmap = mImgView.saveBitmapDoodle();

                // 调试，输出图像
                debug(bitmap);

                new Thread(() -> {
                    int w = bitmap.getWidth();
                    int h = bitmap.getHeight();

                    int[][] pixels = new int[w][h];

                    for (int x = 0; x < w; x++) {
                        for (int y = 0; y < h; y++) {
                            int color = bitmap.getPixel(x, y);
                            pixels[x][y] = Color.red(color) + Color.green(color) + Color.blue(color);
                        }
                    }

                    FileIOUtils.writeFileFromString(new File(PathUtils.getExternalAppCachePath() + "/test.txt"), Arrays.deepToString(pixels));
                }).start();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.eraseMode) {
            mImgView.setMode(DOODLE);
        } else if (id == R.id.moveMode) {
            mImgView.setMode(NONE);
        }
    }

    private void debug(Bitmap bitmap) {
        try {
            File file = new File(PathUtils.getExternalAppCachePath() + "/test.png");
            if (file.exists()) {
                file.delete();
            }

            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
