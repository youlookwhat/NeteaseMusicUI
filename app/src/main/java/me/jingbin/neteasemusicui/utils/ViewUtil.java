package me.jingbin.neteasemusicui.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ViewUtil {

    public static void setVisibleHeight(View view, int height) {
        if (height < 0) {
            height = 0;
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        lp.height = height;
        view.setLayoutParams(lp);
    }

    public static void setMarginHeight(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams = null;
        //获取view的margin设置参数
        if (params instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) params;
        } else {
            //不存在时创建一个新的参数
            marginParams = new ViewGroup.MarginLayoutParams(params);
        }
        //设置margin
        marginParams.setMargins(0, height, 0, 0);
        view.setLayoutParams(marginParams);
        view.requestLayout();
    }
}
