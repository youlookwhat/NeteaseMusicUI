package me.jingbin.neteasemusicui.app;

/**
 * Created by jingbin on 2016/11/22.
 */

public class Application extends android.app.Application {

    private static Application cloudReaderApplication;

    public static Application getInstance() {
        return cloudReaderApplication;
    }

    @SuppressWarnings("unused")
    @Override
    public void onCreate() {
        super.onCreate();
        cloudReaderApplication = this;
    }
}
