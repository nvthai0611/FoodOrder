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

    public static void redirect(Context from, Class<?> to, boolean isFinish) {
        Intent intent = new Intent(from, to);
        from.startActivity(intent);
        if (from instanceof Activity && isFinish) {
            ((Activity) from).finish();
        }
    }

    public static void redirect(Context from, Class<?> to, Bundle extra, boolean isFinish) {
        Intent intent = new Intent(from, to);
        if (extra != null) intent.putExtras(extra);
        from.startActivity(intent);
        if (from instanceof Activity && isFinish) {
            ((Activity) from).finish();
        }
    }
}
