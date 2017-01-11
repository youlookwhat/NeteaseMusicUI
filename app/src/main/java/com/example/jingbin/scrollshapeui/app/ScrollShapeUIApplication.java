package com.example.jingbin.scrollshapeui.app;

import android.app.Application;

/**
 * Created by jingbin on 2016/11/22.
 */

public class ScrollShapeUIApplication extends Application {

    private static ScrollShapeUIApplication cloudReaderApplication;

    public static ScrollShapeUIApplication getInstance() {
        return cloudReaderApplication;
    }

    @SuppressWarnings("unused")
    @Override
    public void onCreate() {
        super.onCreate();
        cloudReaderApplication = this;
    }
}
