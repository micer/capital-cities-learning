package eu.micer.capitalcitieslearning.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.Random;

public class CommonUtil {
    private static final String TAG = CommonUtil.class.getSimpleName();
    private static CommonUtil instance;

    private Random random;

    private CommonUtil(Random random) {
        this.random = random;
    }

    public static CommonUtil getInstance() {
        if (instance == null) {
            synchronized (CommonUtil.class) {
                if (instance == null) {
                    instance = new CommonUtil(new Random());
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

    public <T> T getRandomItem(@NonNull List<T> list) {
        return list.get(random.nextInt(list.size()));
    }
}
