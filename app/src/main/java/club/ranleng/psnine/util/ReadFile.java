package club.ranleng.psnine.util;

import android.app.Activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import club.ranleng.psnine.util.AndroidUtilCode.LogUtils;
import club.ranleng.psnine.util.AndroidUtilCode.Utils;

/**
 * Created by ran on 12/07/2017.
 */

public class ReadFile {

    public static String read(String filename){
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
        return "";
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
}
