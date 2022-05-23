package com.ninelock.editor.photo.views.base;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.ninelock.editor.photo.R;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

public class BaseActivity extends AppCompatActivity {

    private final Handler mHandler = new Handler();

    private QMUITipDialog mTipDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImmersionBar.with(this)
                .statusBarColor(R.color.title_bar_color)
                .autoDarkModeEnable(true)
                .fitsSystemWindows(true)
                .init();
    }

    protected void showTip(String text, int type) {
        mTipDialog = new QMUITipDialog.Builder(this)
                .setIconType(type)
                .setTipWord(text)
                .create(true);
        mTipDialog.show();
        mHandler.postDelayed(() -> mTipDialog.dismiss(), 2000);
    }

    protected void showLoading() {
        mTipDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create(true);
        mTipDialog.show();
    }

    protected void hideLoading() {
        mTipDialog.dismiss();
    }
}
