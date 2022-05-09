package com.ninelock.editor.photo.views.erase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ninelock.editor.photo.R;
import com.ninelock.editor.photo.views.base.BaseActivity;

public class ErasePenEditorActivity extends BaseActivity {

    private static final String FILEPATH = "FILEPATH";

    public static void goEditor(Context context, String filepath) {
        Intent intent = new Intent(context, ErasePenEditorActivity.class);

        intent.putExtra(FILEPATH, filepath);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_erase_pen);

        initEvent();
    }

    private void initEvent() {

    }
}
