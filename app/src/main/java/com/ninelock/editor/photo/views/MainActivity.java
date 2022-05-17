package com.ninelock.editor.photo.views;

import static com.luck.picture.lib.config.SelectModeConfig.SINGLE;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dlut.iiauapp.InpaintingNative;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.ninelock.editor.photo.R;
import com.ninelock.editor.photo.config.GlideEngine;
import com.ninelock.editor.photo.config.picture.SandboxFileEngine;
import com.ninelock.editor.photo.config.picture.SelectorWhiteStyle;
import com.ninelock.editor.photo.views.base.BaseActivity;
import com.ninelock.editor.photo.views.erase.ErasePenEditorActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK && null != result.getData()) {
            try {
                // 保存到应用目录
                Uri returnUri = result.getData().getData();
                if (returnUri == null) {
                    ToastUtils.showLong("取消选择");
                    return;
                }

                Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
                returnCursor.moveToFirst();

                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                String filename = returnCursor.getString(nameIndex);

                String tempPath = PathUtils.getExternalAppFilesPath() + "/temp_photo";
                File tempFolder = new File(tempPath);
                if (!tempFolder.exists()) {
                    tempFolder.mkdirs();
                }

                final File tempFile = new File(tempPath, filename);
                if (tempFile.exists()) {
                    tempFile.delete();
                }
                FileOutputStream outputStream = new FileOutputStream(tempFile);
                InputStream inputStream = getContentResolver().openInputStream(returnUri);
                byte[] b = new byte[1024];
                int length;
                while ((length = inputStream.read(b)) > 0) {
                    outputStream.write(b, 0, length);
                }
                outputStream.flush();
                inputStream.close();
                outputStream.close();

                String tempPhotoPath = tempFile.getAbsolutePath();
                ErasePenEditorActivity.goEditor(this, tempPhotoPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initEvent();
    }

    private void initEvent() {

        findViewById(R.id.selectFile).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*").addCategory(Intent.CATEGORY_OPENABLE);
            mLauncher.launch(intent);
        });

        findViewById(R.id.selectPhoto).setOnClickListener(v -> {
            // 选择图片，拷贝到应用所属目录
            PictureSelector.create(this)
                    .openGallery(SelectMimeType.ofImage())
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .setSandboxFileEngine(new SandboxFileEngine())
                    .setSelectorUIStyle(SelectorWhiteStyle.get(this))
                    .setSelectionMode(SINGLE)
                    .isEmptyResultReturn(true)
                    .forResult(new OnResultCallbackListener<>() {
                        @Override
                        public void onResult(ArrayList<LocalMedia> result) {
                            if (CollectionUtils.isNotEmpty(result)) {
                                // 获取选择的文件
                                LocalMedia localMedia = result.get(0);
                                // 跳转编辑页面
                                ErasePenEditorActivity.goEditor(MainActivity.this, localMedia.getSandboxPath());
                            }
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
        });

        findViewById(R.id.runJna).setOnClickListener(v -> {
//            com.dlut.test.MainActivity iiauApp = new com.dlut.test.MainActivity();
//            String test = iiauApp.stringFromJNI();
//            System.out.println(test);

            InpaintingNative inpaintingNative = new InpaintingNative();
            int out = inpaintingNative.inpainting("/storage/emulated/0/$MuMu共享文件夹/aaa.png", "/storage/emulated/0/$MuMu共享文件夹/mask.png","/storage/emulated/0/$MuMu共享文件夹/out.png");
            if(out>0){
                System.out.println("处理成功");
            }else{
                System.out.println("处理失败");
            }
            inpaintingNative.deinit();
        });
    }
}