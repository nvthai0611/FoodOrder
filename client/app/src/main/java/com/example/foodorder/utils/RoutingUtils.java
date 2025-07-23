package com.example.foodorder.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Simple helper class for Routing with Intent
 */
public class RoutingUtils {
    private RoutingUtils() {
    }

    public static final Bundle NO_EXTRAS = null;
    public static final boolean ACTIVITY_FINISH = true;
    public static final boolean ACTIVITY_KEEP = false;

    public static void redirect(Context from, Class<?> to, Bundle extra, boolean isFinish, int... flags) {
        Intent intent = new Intent(from, to);
        if (extra != null) intent.putExtras(extra);
        if (flags.length > 0) {
            int finalMask = 0;
            for (int flag : flags) {
                finalMask |= flag;
            }
            intent.setFlags(finalMask);
        }
        from.startActivity(intent);
        if (from instanceof Activity && isFinish) {
            ((Activity) from).finish();
        }
    }
}
