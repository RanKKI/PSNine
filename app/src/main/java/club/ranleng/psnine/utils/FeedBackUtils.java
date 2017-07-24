package club.ranleng.psnine.utils;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

public class FeedBackUtils {
    /**
     *为单个view设置点击效果，高版本带涟漪反馈
     * @param normalColor 未点击的颜色
     * @param pressColor  按下的颜色
     * @param view  目标view
     */
    public static void setOnclickfeedBack(int normalColor, int pressColor, View view) {
        Drawable bgDrawble;
        ColorDrawable drawablePressed = new ColorDrawable(pressColor);//分别解析两种颜色为colordrawble
        ColorDrawable drawableNormal = new ColorDrawable(normalColor);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {//高版本设置RippleDrawable 而低版本设置 StateListDrawable也就是selector
            ColorStateList stateList = ColorStateList.valueOf(pressColor);
            bgDrawble = new RippleDrawable(stateList, drawableNormal, drawablePressed);
        } else {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed);
            stateListDrawable.addState(new int[]{-android.R.attr.state_pressed}, drawableNormal);
            bgDrawble = stateListDrawable;
        }
        view.setBackground(bgDrawble);
    }

    /**
     *支持同时设置多个view
     * @param normalColor
     * @param pressColor
     * @param views 目标view群
     */
    public static void setOnclickfeedBack(int normalColor, int pressColor, View... views) {
        for (int i = 0, size = views.length; i < size; i++) {
            View view = views[i];
            setOnclickfeedBack(normalColor, pressColor, view);
        }
    }

}
