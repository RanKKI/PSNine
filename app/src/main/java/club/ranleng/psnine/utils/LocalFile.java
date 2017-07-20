package club.ranleng.psnine.utils;

import android.app.Activity;
import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

public class LocalFile {

    public static Boolean isFileExists(String filename) {
        File file = new File(Utils.getContext().getFilesDir() + "/" + filename);
        return file.exists();
    }

    public static String read(String filename) {
        File file = new File(Utils.getContext().getFilesDir() + "/" + filename);
        if (file.exists()) {
            try {
                FileInputStream inputStream = Utils.getContext().openFileInput(filename);
                byte[] bytes = new byte[1024];
                int len = 0;
                len = inputStream.read(bytes);
                inputStream.close();
                return new String(bytes, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file.delete()) {
                LogUtils.d("已删除" + filename);
            }
        }
        return null;
    }

    public static void del(String filename) {
        File file = new File(Utils.getContext().getFilesDir() + "/" + filename);
        if (isFileExists(filename)) {
            if (file.delete()) {
                LogUtils.d("已删除" + filename);
            }
        }

    }

    public static void save(String filename, String content) {

        try {
            FileOutputStream outputStream = Utils.getContext().openFileOutput(filename,
                    Activity.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void save(String filename, EditText editText) {
        save(filename, TextUtils.toS(editText));
    }

    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }
}
