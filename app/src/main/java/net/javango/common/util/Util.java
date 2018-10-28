package net.javango.common.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Miscellaneous utilities
 */
public class Util {

    private Util() {
        // utility
    }

    public static void copy(File src, File dst) throws IOException {
        try (FileInputStream is = new FileInputStream(src); FileOutputStream os = new FileOutputStream(dst)) {
            FileChannel inCh = is.getChannel();
            FileChannel outCh = os.getChannel();
            inCh.transferTo(0, inCh.size(), outCh);
        }
    }

    /**
     * Checks a single permission and if not grated requests it. Returns a flag indicating the status of permission
     */
    public static boolean checkPermission(Activity activity, String permission, int requestCode) {
        if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            return false;
        } else {
            return true;
        }
    }

    /**
     *  Checks if external storage is available for read and write
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     *  Checks if external storage is available to at least read
     */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
            Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
