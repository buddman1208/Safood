
/*
 * Created by KOHA on 2016.
 * Copyright by Sunrin Internet High School EDCAN
 * All rights reversed.
 */

package kr.edcan.safood.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.util.Log;
import android.widget.Toast;

public class SafoodHelper {

    Camera camera;
    private Context c;

    public SafoodHelper(Context c) {
        this.c = c;
    }

    public boolean checkCameraHardware() {
        return (c.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA));
    }

    public Camera getCameraInstance() {
        return camera.open();
    }

    public int colorCombine(int c1, int c2, float offset) {
        int result;
        result = Color.rgb(
                (int) (Color.red(c1) - (Color.red(c1) - Color.red(c2)) * offset),
                (int) (Color.green(c1) - (Color.green(c1) - Color.green(c2)) * offset),
                (int) (Color.blue(c1) - (Color.blue(c1) - Color.blue(c2)) * offset)
        );
        return result;
    }
}
