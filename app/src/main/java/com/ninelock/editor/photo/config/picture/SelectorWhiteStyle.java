package com.ninelock.editor.photo.config.picture;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import com.luck.picture.lib.style.BottomNavBarStyle;
import com.luck.picture.lib.style.PictureSelectorStyle;
import com.luck.picture.lib.style.SelectMainStyle;
import com.luck.picture.lib.style.TitleBarStyle;

public class SelectorWhiteStyle {

    public static PictureSelectorStyle get(Context context) {

        PictureSelectorStyle selectorStyle = new PictureSelectorStyle();

        TitleBarStyle whiteTitleBarStyle = new TitleBarStyle();
        whiteTitleBarStyle.setTitleBackgroundColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_white));
        whiteTitleBarStyle.setTitleDrawableRightResource(com.ninelock.editor.photo.R.mipmap.ic_orange_arrow_down);
        whiteTitleBarStyle.setTitleLeftBackResource(com.ninelock.editor.photo.R.mipmap.ic_black_back);
        whiteTitleBarStyle.setTitleTextColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_black));
        whiteTitleBarStyle.setTitleCancelTextColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_53575e));
        whiteTitleBarStyle.setDisplayTitleBarLine(true);

        BottomNavBarStyle whiteBottomNavBarStyle = new BottomNavBarStyle();
        whiteBottomNavBarStyle.setBottomNarBarBackgroundColor(Color.parseColor("#EEEEEE"));
        whiteBottomNavBarStyle.setBottomPreviewSelectTextColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_53575e));

        whiteBottomNavBarStyle.setBottomPreviewNormalTextColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_9b));
        whiteBottomNavBarStyle.setBottomPreviewSelectTextColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_fa632d));
        whiteBottomNavBarStyle.setCompleteCountTips(false);
        whiteBottomNavBarStyle.setBottomEditorTextColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_53575e));
        whiteBottomNavBarStyle.setBottomOriginalTextColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_53575e));

        SelectMainStyle selectMainStyle = new SelectMainStyle();
        selectMainStyle.setStatusBarColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_white));
        selectMainStyle.setDarkStatusBarBlack(true);
        selectMainStyle.setSelectNormalTextColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_9b));
        selectMainStyle.setSelectTextColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_fa632d));
        selectMainStyle.setPreviewSelectBackground(com.ninelock.editor.photo.R.drawable.ps_white_preview_selector);
        selectMainStyle.setSelectBackground(com.luck.picture.lib.R.drawable.ps_checkbox_selector);
        selectMainStyle.setSelectText(context.getString(com.luck.picture.lib.R.string.ps_done_front_num));
        selectMainStyle.setMainListBackgroundColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_white));

        selectorStyle.setTitleBarStyle(whiteTitleBarStyle);
        selectorStyle.setBottomBarStyle(whiteBottomNavBarStyle);
        selectorStyle.setSelectMainStyle(selectMainStyle);

        return selectorStyle;
    }
}
