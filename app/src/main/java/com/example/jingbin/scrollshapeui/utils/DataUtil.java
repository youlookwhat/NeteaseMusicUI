package com.example.jingbin.scrollshapeui.utils;

import com.example.jingbin.scrollshapeui.R;
import com.example.jingbin.scrollshapeui.bean.DynamicBean;

import java.util.ArrayList;

/**
 * 生成数据工具类
 *
 * @author jingbin
 */
public class DataUtil {


    /**
     * 一般item的数据
     */
    public static ArrayList<DynamicBean> get(int num) {
        ArrayList<DynamicBean> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            DynamicBean bean = new DynamicBean();
            bean.setContent("数据展示");
            list.add(bean);
        }
        return list;
    }

    public static ArrayList<DynamicBean> getContent() {
        ArrayList<DynamicBean> list = new ArrayList<>();
        DynamicBean bean = new DynamicBean();
        bean.setContent(CommonUtils.getString(R.string.string_intr));
        list.add(bean);
        return list;
    }

}
