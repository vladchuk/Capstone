package net.javango.carcare.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Util {

    public static void copy(File src, File dst) throws IOException {
        try (FileInputStream is = new FileInputStream(src); FileOutputStream os = new FileOutputStream(dst)) {
            FileChannel inCh = is.getChannel();
            FileChannel outCh = os.getChannel();
            inCh.transferTo(0, inCh.size(), outCh);
        }
    }
}
