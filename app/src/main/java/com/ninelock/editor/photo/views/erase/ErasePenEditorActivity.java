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

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ImageUtils;
import com.dlut.iiauapp.InpaintingNative;
import com.ninelock.editor.photo.R;
import com.ninelock.editor.photo.views.base.BaseActivity;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

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

    private AppCompatImageView back;
    private AppCompatImageView undo;
    private AppCompatImageView redo;
    private AppCompatImageView refresh;
    private AppCompatTextView finish;

    private IMGView mImgView;
    private SeekBar mPenSize;

    private int step;
    private int maxStep;
    private String projectDir;
    private String projectOriginFilepath;
    private String projectCurrentFilepath;
    private String type = ERASE_TYPE;

    private boolean showOrigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_erase_pen);

        mFilepath = getIntent().getStringExtra(FILEPATH);
        mFilename = getIntent().getStringExtra(FILENAME);

        step = 0;
        projectDir = null;
        showOrigin = false;

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
        // ?????????
        back = findViewById(R.id.back);
        undo = findViewById(R.id.undo);
        redo = findViewById(R.id.redo);
        refresh = findViewById(R.id.refresh);
        finish = findViewById(R.id.finish);

        undo.setVisibility(View.INVISIBLE);
        redo.setVisibility(View.INVISIBLE);

        // ??????
        mImgView = findViewById(R.id.imgView);
        Bitmap bitmap = getBitmap(mFilepath);
        if (bitmap != null) {
            mImgView.setImageBitmap(bitmap);
        }

        // ????????????
        mPenSize = findViewById(R.id.penSize);
        mPenSize.setProgress(40);

        // ????????????
        type = ERASE_TYPE;
    }

    private void initEvent() {
        back.setOnClickListener(v -> finish());

        // ??????
        undo.setOnClickListener(v -> {
            // ?????????????????????
            step -= 1;
            String filepath;
            if (step == 0) {
                // ????????????
                filepath = projectOriginFilepath;
            } else {
                filepath = projectDir + "result_" + step + ".png";
            }
            Bitmap bitmap = getBitmap(filepath);
            if (bitmap != null) {
                mImgView.setImageBitmap(bitmap);
            }

            // ????????????????????????
            redo.setVisibility(View.VISIBLE);
            // ????????????????????????????????????????????????
            if (step == 0) {
                undo.setVisibility(View.INVISIBLE);
            }
        });

        // ??????
        redo.setOnClickListener(v -> {
            // ?????????????????????
            step += 1;
            if (step <= maxStep) {
                String filepath = projectDir + "result_" + step + ".png";
                Bitmap bitmap = getBitmap(filepath);
                if (bitmap != null) {
                    mImgView.setImageBitmap(bitmap);
                }
                undo.setVisibility(View.VISIBLE);
                if (step == maxStep) {
                    redo.setVisibility(View.INVISIBLE);
                }
            } else {
                redo.setVisibility(View.INVISIBLE);
            }
        });
        // ???????????????
        refresh.setOnClickListener(v -> {
            // ??????
            showAsk("??????", "????????????????????????", (dialog, index) -> {
                // ????????????
                step = 0;
                maxStep = 0;

                // ??????????????????????????????
                Bitmap bitmap = getBitmap(mFilepath);
                if (bitmap != null) {
                    mImgView.setImageBitmap(bitmap);
                }

                // ???????????????
                dialog.dismiss();
            });
        });

        // ??????
        finish.setOnClickListener(v -> {
            mImgView.reset();
            ImageUtils.save2Album(mImgView.saveBitmap(), Bitmap.CompressFormat.PNG);
            showTip("???????????????????????????", QMUITipDialog.Builder.ICON_TYPE_SUCCESS);
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
            int doodlesSize = mImgView.getDoodlesSize();
            if (doodlesSize == 0) {
                return;
            }

            // ??????
            if (step == 1 && RESTORE_TYPE.equals(type)) {
                showTip("????????????????????????????????????", QMUITipDialog.Builder.ICON_TYPE_FAIL);
                return;
            }

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
            // ????????????
            type = ERASE_TYPE;

            // ??????????????????
            mImgView.clearDoodles();
            mImgView.setMode(DOODLE);
            mImgView.setPenColor(0x80FF0000);
        } else if (id == R.id.restoreMode) {
            // ????????????
            type = RESTORE_TYPE;

            // ??????????????????
            mImgView.clearDoodles();
            mImgView.setMode(DOODLE);
            mImgView.setPenColor(0x80FF0000);
        } else if (id == R.id.moveMode) {
            mImgView.setMode(NONE);
        } else if (id == R.id.compare) {
            if (!showOrigin) {
                showOrigin = true;
                mImgView.setMode(NONE);

                // ????????????
                // ??????????????????????????????
                Bitmap bitmap = getBitmap(mFilepath);
                if (bitmap != null) {
                    mImgView.setImageBitmap(bitmap);
                }
            } else {
                showOrigin = false;

                // ??????????????????
                Bitmap bitmap = getBitmap(projectCurrentFilepath);
                if (bitmap != null) {
                    mImgView.setImageBitmap(bitmap);
                }
            }
        }
    }

    private void runNative() throws Exception {
        if (step == 0) {
            // ??????????????????
            projectDir = getExternalFilesDir(null) + "/project_" + mFilename + "/";
            File projectFolder = new File(projectDir);
            if (projectFolder.exists()) {
                projectFolder.delete();
            } else {
                projectFolder.mkdirs();
            }

            // ????????????
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

        step++;
        maxStep = step;

        // ??????????????????
        File maskFile = new File(projectDir + "mask_" + step + ".png");
        if (maskFile.exists()) {
            maskFile.delete();
        }
        FileOutputStream stream = new FileOutputStream(maskFile);
        mImgView.saveBitmapDoodle().compress(Bitmap.CompressFormat.PNG, 100, stream);
        stream.flush();
        stream.close();

        // ??????????????????
        String resultFilepath = projectDir + "result_" + step + ".png";
        File resultFile = new File(resultFilepath);
        if (resultFile.exists()) {
            resultFile.delete();
        }

        // ??????jni??????
        InpaintingNative inpaintingNative = new InpaintingNative();
        if (ERASE_TYPE.equals(type)) {
            // ??????
            inpaintingNative.inpainting(projectCurrentFilepath, maskFile.getAbsolutePath(), resultFilepath);
        } else if (RESTORE_TYPE.equals(type)) {
            // ??????
            inpaintingNative.recover(projectOriginFilepath, projectCurrentFilepath, maskFile.getAbsolutePath(), resultFilepath);
        }

        projectCurrentFilepath = resultFilepath;

        runOnUiThread(() -> {
            // ????????????
            mImgView.reset();

            // ???????????????????????????????????????
            undo.setVisibility(View.VISIBLE);
            redo.setVisibility(View.INVISIBLE);

            // ???????????????
            Bitmap bitmap = getBitmap(resultFilepath);
            if (bitmap != null) {
                mImgView.setImageBitmap(bitmap);
            }
        });
    }
}
