package com.ninelock.editor.photo.views.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ninelock.editor.photo.R;
import com.gyf.immersionbar.ImmersionBar;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImmersionBar.with(this)
                .statusBarColor(R.color.title_bar_color)
                .autoDarkModeEnable(true)
                .fitsSystemWindows(true)
                .init();
    }
}
