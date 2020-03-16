package me.jingbin.neteasemusicui.utils;

import android.content.res.Resources;

import me.jingbin.neteasemusicui.app.Application;


/**
 * Created by jingbin on 2016/11/22.
 * 获取原生资源
 */
public class CommonUtils {


    private static Resources getResoure() {
        return Application.getInstance().getResources();
    }

    public static String[] getStringArray(int resid) {
        return getResoure().getStringArray(resid);
    }

    public static String getString(int resid) {
        return getResoure().getString(resid);
    }

    public static float getDimens(int resId) {
        return getResoure().getDimension(resId);
    }



}
