package com.example.foodorder.utils;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Simple helper class for Routing with Intent
 */
public class Routing {
    private Routing() {
    }

    public static void redirect(Context from, Class<?> to) {
        Intent intent = new Intent(from, to);
        from.startActivity(intent);
        if (from instanceof Activity) {
            ((Activity) from).finish();
        }
    }

    public static void redirect(Context from, Class<?> to, Bundle extra) {
        Intent intent = new Intent(from, to);
        if (extra != null) intent.putExtras(extra);
        from.startActivity(intent);
        if (from instanceof Activity) {
            ((Activity) from).finish();
        }
    }
}
