package com.ninelock.editor.photo.views;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ninelock.editor.photo.R;
import com.ninelock.editor.photo.views.base.BaseActivity;
import com.ninelock.editor.photo.views.erase.ErasePenEditorActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

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
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                String filename = returnCursor.getString(nameIndex);
                String fileSize = Long.toString(returnCursor.getLong(sizeIndex));

                String tempPath = PathUtils.getExternalAppDataPath() + "/temp_photo";
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

                // TODO: 跳转页面
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
            // 点击左上，选择外置存储后，再选择文件
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*").addCategory(Intent.CATEGORY_OPENABLE);
            mLauncher.launch(intent);
        });


        findViewById(R.id.selectPhoto).setOnClickListener(v -> {
            // 选择图片，拷贝到应用所属目录
        });
    }
}