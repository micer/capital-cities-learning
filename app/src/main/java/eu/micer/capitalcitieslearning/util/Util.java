package eu.micer.capitalcitieslearning.util;

import android.support.annotation.Nullable;

import java.util.List;

public class Util {
    private static final String TAG = Util.class.getSimpleName();
    private static Util instance;

    public static Util getInstance() {
        if (instance == null) {
            synchronized (Util.class) {
                if (instance == null) {
                    instance = new Util();
                }
            }
        }
        return instance;
    }

    public boolean isNullOrEmpty(@Nullable String string) {
        return string == null || string.isEmpty();
    }

    public boolean isNullOrEmpty(@Nullable List list) {
        return list == null || list.isEmpty();
    }
}
