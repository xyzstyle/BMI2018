package xyz.util;

import android.app.Activity;
import android.content.Context;

/**
 * Created by xyz on 2018/9/30.
 */

public class Util {
    public static <T> T findMyView(Activity activity,  int res) {
        return (T) activity.findViewById(res);
    }
}
