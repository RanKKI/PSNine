package club.ranleng.psnine.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import club.ranleng.psnine.utils.SharedPerferenceUtils;


public class KEY {

    public static final int TYPE_TOPIC = 1448;
    public static final int TYPE_GENE = 6107;
    public static final int TYPE_GENE_ELE = 6108;
    public static final int TYPE_PLUS = 7890;
    public static final int TYPE_OPENBOX = 5427;
    public static final int TYPE_GUIDE = 9820;
    public static final int TYPE_NOTICE = 7775;
    public static final int TYPE_COMMENT = 1238;
    public static final int TYPE_QA = 1218;

    public static final int TYPE_FAV_GENE = 999;
    public static final int TYPE_FAV_TOPIC = 998;



    public static final Map<Integer, String> TYPE_NAME = new HashMap<Integer, String>() {{
        put(TYPE_GENE, "gene");
        put(TYPE_TOPIC, "topic");
        put(TYPE_OPENBOX, "openbox");
        put(TYPE_PLUS, "plus");
        put(TYPE_GUIDE, "guide");
        put(TYPE_NOTICE,"notice");
        put(TYPE_COMMENT, "comment");
        put(TYPE_QA, "qa");
    }};

    public static String INT_TYPE(int type){
        if(type == TYPE_GENE || type == TYPE_FAV_GENE){
            return "gene";
        }else {
            return "topic";
        }
    }


    public static final int SEARCH = 7405;
    public static final int PICKIMG = 5224;
    public static final int SETTING = 2658;
    public static final int IMAGE = 671;
    public static final int TROPHYTIPS = 860;
    public static final int TROPHY = 6268;

    public static final int FORM_CARAMA = 14;
    public static final int FORM_LOCAL = 15;

    public static final int PSN_GAME = 3743;
    public static final int PSN_MSG = 3744;

    public static final int REQUEST_PERMISSION_READ_EXTERNAL = 21;

    public static final int REQUEST_PICKIMG = 976;

    //PREF KEY
    public static final String KEY_PREF_PRELOAD = "settings_preload";

    public static final String KEY_PREF_OB = "settings_ob";

    public static final String KEY_PREF_EMOJI = "settings_emoji_dialog";

    public static final String KEY_PREF_SINGLELINE = "settings_single_line";

    public static final String KEY_PREF_IMAGES_QUALITY = "settings_images_quality";
    public static final String KEY_PREF_TABS = "settings_tabs";

    public static Boolean PREF_PRELOAD = false;
    public static String PREF_OB = "obdate";
    public static Boolean PREF_EMOJI = true;
    public static Boolean PREF_SINGLELINE = true;
    public static Boolean PREF_IMAGESQUALITY = true;

    public static void initSetting(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        PREF_PRELOAD = sharedPref.getBoolean(KEY.KEY_PREF_PRELOAD, false);
        PREF_OB = sharedPref.getString(KEY.KEY_PREF_OB, "obdate");
        PREF_EMOJI = sharedPref.getBoolean(KEY.KEY_PREF_EMOJI, true);
        PREF_SINGLELINE = sharedPref.getBoolean(KEY.KEY_PREF_SINGLELINE, true);
        PREF_IMAGESQUALITY = sharedPref.getBoolean(KEY.KEY_PREF_IMAGES_QUALITY, true);
        if(getTabs() == null){
            List<Integer> list = new ArrayList<Integer>(){{
                add(TYPE_GENE);
                add(TYPE_TOPIC);
                add(TYPE_OPENBOX);
                add(TYPE_GUIDE);
                add(TYPE_PLUS);
                add(TYPE_QA);
            }};
            setTabs(list);
        }
    }

    public static final Map<Integer, String> TYPE_NAME_CN = new HashMap<Integer, String>() {{
        put(TYPE_GENE, "基因");
        put(TYPE_TOPIC, "首页");
        put(TYPE_OPENBOX, "开箱");
        put(TYPE_PLUS, "PLUS");
        put(TYPE_GUIDE, "攻略");
        put(TYPE_QA, "问答");
    }};

    public static List<Integer> getTabs(){
        Object object = SharedPerferenceUtils.get("tabs","tabs");
        if(object != null){
            return (List<Integer>) object;
        }
        return null;
    }

    public static void setTabs(List<Integer> list){
        SharedPerferenceUtils.save("tabs","tabs",list);
    }


    public static Map<String, String> EMOJI_URL_STR = new HashMap<String, String>(){{
        put("majiang/1","[大笑]");
        put("majiang/2","[坏笑]");
        put("majiang/3","[XD]");
        put("majiang/4","[NB]");
        put("majiang/5","[渣]");
        put("majiang/6","[憨笑]");
        put("majiang/7","[调皮]");
        put("majiang/8","[喜欢]");
        put("majiang/9","[流汗]");
        put("majiang/10","[犯困]");
        put("majiang/11","[大汗]");
        put("majiang/12","[惊]");
        put("majiang/13","[虚汗]");
        put("majiang/14","[委屈]");
        put("majiang/15","[无视]");
        put("majiang/16","[撒娇]");
        put("majiang/17","[害羞]");
        put("majiang/18","[石化]");
        put("majiang/19","[流泪]");
        put("majiang/20","[闭嘴]");
        put("majiang/21","[囧]");
        put("majiang/22","[抽烟]");
        put("majiang/23","[捂嘴]");
        put("majiang/24","[晕菜]");
        put("majiang/25","[喝茶]");
        put("majiang/26","[+1]");
        put("majiang/27","[卖萌]");
        put("majiang/28","[认真]");
        put("majiang/29","[哭]");
        put("majiang/30","[吃屎]");
        put("majiang/31","[大神]");
        put("majiang/32","[墨镜]");
        put("majiang/33","[冒光]");
        put("majiang/34","[口水]");
        put("majiang/35","[鼻血]");
        put("majiang/36","[瞎]");
        put("majiang/37","[吃瘪]");
        put("majiang/38","[眼镜]");
        put("majiang/39","[气愤]");
        put("majiang/40","[中箭]");
        put("majiang/41","[DOGE]");
        put("shoubing/1","[叉]");
        put("shoubing/2","[方块]");
        put("shoubing/3","[三角]");
        put("shoubing/4","[圆圈]");
        put("shoubing/5","[上]");
        put("shoubing/6","[下]");
        put("shoubing/7","[左]");
        put("shoubing/8","[右]");
        put("shoubing/9","[D-PAD]");
        put("shoubing/10","[L1]");
        put("shoubing/11","[L2]");
        put("shoubing/12","[L3]");
        put("shoubing/13","[R1]");
        put("shoubing/14","[R2]");
        put("shoubing/15","[R3]");
        put("shoubing/16","[SELECT]");
        put("shoubing/17","[START]");
        put("shoubing/18","[PS]");
        put("shoubing/19","[OPTION]");
        put("shoubing/20","[SHARE]");
        put("shoubing/21","[T-PAD]");
        put("shoubing/22","[LS]");
        put("shoubing/23","[RS]");
        put("shoubing/24","[LS-上]");
        put("shoubing/25","[LS-右上]");
        put("shoubing/26","[LS-右]");
        put("shoubing/27","[LS-右下]");
        put("shoubing/28","[LS-下]");
        put("shoubing/29","[LS-左下]");
        put("shoubing/30","[LS-左]");
        put("shoubing/31","[LS-左上]");
        put("shoubing/32","[RS-上]");
        put("shoubing/33","[RS-右上]");
        put("shoubing/34","[RS-右]");
        put("shoubing/35","[RS-右下]");
        put("shoubing/36","[RS-下]");
        put("shoubing/37","[RS-左下]");
        put("shoubing/38","[RS-左]");
        put("shoubing/39","[RS-左上]");
        put("alu/1","[阿鲁憨笑]");
        put("alu/2","[阿鲁皱眉]");
        put("alu/3","[阿鲁不开心]");
        put("alu/4","[阿鲁阴笑]");
        put("alu/5","[阿鲁吃惊]");
        put("alu/6","[阿鲁懵逼]");
        put("alu/7","[阿鲁委屈]");
        put("alu/8","[阿鲁茫然]");
        put("alu/9","[阿鲁XD]");
        put("alu/10","[阿鲁崇拜]");
        put("alu/11","[阿鲁淫笑]");
        put("alu/12","[阿鲁獠牙]");
        put("alu/13","[阿鲁哭]");
        put("alu/14","[阿鲁茫茫然]");
        put("alu/15","[阿鲁脸红]");
        put("alu/16","[阿鲁亲亲]");
        put("alu/17","[阿鲁出汗]");
        put("alu/18","[阿鲁瞌睡]");
        put("alu/19","[阿鲁墨镜]");
        put("alu/20","[阿鲁抠鼻]");
        put("alu/21","[阿鲁吃糖]");
        put("alu/22","[阿鲁出血]");
        put("alu/23","[阿鲁口水]");
        put("alu/24","[阿鲁吐了]");
        put("alu/25","[阿鲁鼻涕]");
        put("alu/26","[阿鲁绷带]");
        put("alu/27","[阿鲁吐舌]");
        put("alu/28","[阿鲁闭嘴]");
        put("alu/29","[阿鲁扶镜]");
        put("alu/30","[阿鲁打码]");
        put("alu/31","[阿鲁吐血]");
        put("alu/32","[阿鲁冒火]");
        put("alu/33","[阿鲁冻结]");
        put("alu/34","[阿鲁挂了]");
        put("alu/35","[阿鲁点赞]");
        put("alu/36","[阿鲁异议]");
        put("alu/37","[阿鲁无奈]");
        put("alu/38","[阿鲁开森]");
        put("alu/39","[阿鲁捂脸]");
        put("alu/40","[阿鲁害羞]");
        put("alu/41","[阿鲁脸疼]");
        put("alu/42","[阿鲁琢磨]");
        put("alu/43","[阿鲁鼓掌]");
        put("alu/44","[阿鲁DOGE]");
    }};

}
