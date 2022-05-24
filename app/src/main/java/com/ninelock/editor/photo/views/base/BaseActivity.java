package com.ninelock.editor.photo.views.base;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.ninelock.editor.photo.R;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
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

    protected void showAsk(String title, String message, QMUIDialogAction.ActionListener listener) {
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle(title)
                .setMessage(message)
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction("确定", listener)
                .show();

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
