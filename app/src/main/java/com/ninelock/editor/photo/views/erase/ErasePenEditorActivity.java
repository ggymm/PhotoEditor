package com.ninelock.editor.photo.views.erase;

import static com.ninelock.editor.photo.utils.FileUtils.getFileExt;
import static com.ninelock.editor.photo.views.erase.Constant.ERASE_TYPE;
import static com.ninelock.editor.photo.views.erase.Constant.RESTORE_TYPE;
import static me.minetsh.imaging.core.IMGMode.DOODLE;
import static me.minetsh.imaging.core.IMGMode.NONE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.blankj.utilcode.util.ImageUtils;
import com.dlut.iiauapp.InpaintingNative;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.ninelock.editor.photo.R;
import com.ninelock.editor.photo.views.base.BaseActivity;

import java.io.File;
import java.io.FileOutputStream;

import me.minetsh.imaging.core.file.IMGDecoder;
import me.minetsh.imaging.core.file.IMGFileDecoder;
import me.minetsh.imaging.core.util.IMGUtils;
import me.minetsh.imaging.view.IMGView;

public class ErasePenEditorActivity extends BaseActivity implements View.OnClickListener {

    private static final String FILEPATH = "FILEPATH";
    private static final String FILENAME = "FILENAME";

    public static void goEditor(Context context, String filepath, String filename) {
        Intent intent = new Intent(context, ErasePenEditorActivity.class);

        intent.putExtra(FILEPATH, filepath);
        intent.putExtra(FILENAME, filename);

        context.startActivity(intent);
    }

    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 1024;

    private String mFilepath;
    private String mFilename;

    private TitleBar mTitleBar;

    private IMGView mImgView;
    private SeekBar mPenSize;

    private int step;
    private String projectDir;
    private String projectOriginFilepath;
    private String projectCurrentFilepath;
    private String type = ERASE_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_erase_pen);

        mFilepath = getIntent().getStringExtra(FILEPATH);
        mFilename = getIntent().getStringExtra(FILENAME);

        step = 1;
        projectDir = null;

        initView();
        initEvent();
    }

    public Bitmap getBitmap(String filepath) {
        IMGDecoder decoder = new IMGFileDecoder(filepath);
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
        // 标题栏
        mTitleBar = findViewById(R.id.titleBar);

        // 图像
        mImgView = findViewById(R.id.imgView);
        Bitmap bitmap = getBitmap(mFilepath);
        if (bitmap != null) {
            mImgView.setImageBitmap(bitmap);
        }

        // 画笔宽度
        mPenSize = findViewById(R.id.penSize);
        mPenSize.setProgress(20);

        // 默认选中
        this.type = ERASE_TYPE;
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
                mImgView.reset();
                ImageUtils.save2Album(mImgView.saveBitmap(), Bitmap.CompressFormat.PNG);
                showSuccessTip("保存到系统相册成功");
            }
        });

        mPenSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 0) {
                    mPenSize.setProgress(1);
                    return;
                }
                if ((int) mImgView.getDoodleWidth() == progress) {
                    return;
                }
                mImgView.setDoodleWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.doErase) {
            showLoading();
            new Thread(() -> {
                try {
                    runNative();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                hideLoading();
            }).start();
        } else if (id == R.id.eraseMode) {
            type = ERASE_TYPE;
            mImgView.setMode(DOODLE);
            mImgView.setPenColor(0x80FF0000);
        } else if (id == R.id.restoreMode) {
            type = RESTORE_TYPE;
            mImgView.setMode(DOODLE);
            mImgView.setPenColor(0x80FFFFFF);
        } else if (id == R.id.moveMode) {
            mImgView.setMode(NONE);
        }
    }

    private void runNative() throws Exception {
        if (step == 1) {
            // 创建工程目录
            projectDir = getExternalFilesDir(null) + "/project_" + mFilename + "/";
            File projectFolder = new File(projectDir);
            if (projectFolder.exists()) {
                projectFolder.delete();
            } else {
                projectFolder.mkdirs();
            }

            // 生成原图
            projectOriginFilepath = projectDir + "origin." + getFileExt(mFilename);
            projectCurrentFilepath = projectOriginFilepath;

            File originFile = new File(projectCurrentFilepath);
            if (originFile.exists()) {
                originFile.delete();
            }
            FileOutputStream stream = new FileOutputStream(originFile);
            mImgView.saveBitmapImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
        }

        // 生成轨迹图片
        File maskFile = new File(projectDir + "mask_" + step + ".png");
        if (maskFile.exists()) {
            maskFile.delete();
        }
        FileOutputStream stream = new FileOutputStream(maskFile);
        mImgView.saveBitmapDoodle().compress(Bitmap.CompressFormat.PNG, 100, stream);
        stream.flush();
        stream.close();

        // 结果图片地址
        String resultFile = projectDir + "result_" + step + ".png";

        // 调用jni方法
        InpaintingNative inpaintingNative = new InpaintingNative();
        if (ERASE_TYPE.equals(type)) {
            // 擦除
            inpaintingNative.inpainting(projectCurrentFilepath, maskFile.getAbsolutePath(), resultFile);
        } else if (RESTORE_TYPE.equals(type)) {
            // 复原
            inpaintingNative.recover(projectOriginFilepath, projectCurrentFilepath, maskFile.getAbsolutePath(), resultFile);
        }

        step++;
        projectCurrentFilepath = resultFile;

        // 清除轨迹
        mImgView.reset();

        // 加载新图片
        Bitmap bitmap = getBitmap(resultFile);
        if (bitmap != null) {
            mImgView.setImageBitmap(bitmap);
        }
    }
}
