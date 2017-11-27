package club.ranleng.psnine.utils;

import android.util.LruCache;

import java.util.ArrayList;

public class LCache {

    private static LruCache<String, ArrayList<Integer>> mCache;

    public static void init() {
        if (mCache != null) {
            return;
        }
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mCache = new LruCache<>(cacheSize);
    }

    public static void add(String url, int width, int height) {
        if (mCache == null) {
            init();
        }
        if (mCache.get(url) != null) {
            return;
        }
        ArrayList<Integer> list = new ArrayList<>();
        list.add(width);
        list.add(height);
        mCache.put(url, list);
    }

    public static ArrayList<Integer> get(String url) {
        if (mCache == null) {
            init();
        }
        return mCache.get(url);
    }

}
