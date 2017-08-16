package club.ranleng.psnine.utils;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import club.ranleng.psnine.R;

public class EmojiUtils {

    private static Map<Integer, String> emoji = new LinkedHashMap<Integer, String>();

    public static void init() {
        emoji.put(R.raw.weiqu, "委屈");
        emoji.put(R.raw.wushi, "无视");
        emoji.put(R.raw.sajiao, "撒娇");
        emoji.put(R.raw.haixiu, "害羞");
        emoji.put(R.raw.shihua, "石化");
        emoji.put(R.raw.liulei, "流泪");
        emoji.put(R.raw.bizui, "闭嘴");
        emoji.put(R.raw.jiong, "囧");
        emoji.put(R.raw.chouyan, "抽烟");
        emoji.put(R.raw.wuzui, "捂嘴");
        emoji.put(R.raw.yuncai, "晕菜");
        emoji.put(R.raw.hecha, "喝茶");
        emoji.put(R.raw.plusone, "+1");
        emoji.put(R.raw.maimeng, "卖萌");
        emoji.put(R.raw.renzhen, "认真");
        emoji.put(R.raw.ku, "哭");
        emoji.put(R.raw.chishi, "吃屎");
        emoji.put(R.raw.dashen, "大神");
        emoji.put(R.raw.mojing, "墨镜");
        emoji.put(R.raw.maoguang, "冒光");
        emoji.put(R.raw.koushui, "口水");
        emoji.put(R.raw.bixue, "鼻血");
        emoji.put(R.raw.xia, "瞎");
        emoji.put(R.raw.chibie, "吃瘪");
        emoji.put(R.raw.yanjing, "眼镜");
        emoji.put(R.raw.qifen, "气愤");
        emoji.put(R.raw.zhongjian, "中箭");
        emoji.put(R.raw.doge, "DOGE");
        emoji.put(R.raw.daxiao, "大笑");
        emoji.put(R.raw.huaixiao, "坏笑");
        emoji.put(R.raw.xd, "XD");
        emoji.put(R.raw.nb, "NB");
        emoji.put(R.raw.zha, "渣");
        emoji.put(R.raw.hanxiao, "憨笑");
        emoji.put(R.raw.tiaopi, "调皮");
        emoji.put(R.raw.xihuan, "喜欢");
        emoji.put(R.raw.liuhan, "流汗");
        emoji.put(R.raw.fankun, "犯困");
        emoji.put(R.raw.dahan, "大汗");
        emoji.put(R.raw.jing, "惊");
        emoji.put(R.raw.xuhan, "虚汗");
        emoji.put(R.raw.weiqu, "委屈");
        emoji.put(R.raw.pscha, "叉");
        emoji.put(R.raw.psfangkuai, "方块");
        emoji.put(R.raw.pssanjiao, "三角");
        emoji.put(R.raw.psyuanquan, "圆圈");
        emoji.put(R.raw.psshang, "上");
        emoji.put(R.raw.psxia, "下");
        emoji.put(R.raw.pszuo, "左");
        emoji.put(R.raw.psyou, "右");
        emoji.put(R.raw.psdpad, "D_PAD");
        emoji.put(R.raw.pslone, "Lone");
        emoji.put(R.raw.psltwo, "Ltwo");
        emoji.put(R.raw.pslthree, "Lthree");
        emoji.put(R.raw.psrone, "Rone");
        emoji.put(R.raw.psrtwo, "Rtwo");
        emoji.put(R.raw.psrthree, "Rthree");
        emoji.put(R.raw.psselect, "SELECT");
        emoji.put(R.raw.psstart, "START");
        emoji.put(R.raw.psps, "PS");
        emoji.put(R.raw.psoption, "OPTION");
        emoji.put(R.raw.psshare, "SHARE");
        emoji.put(R.raw.pstpad, "T_PAD");
        emoji.put(R.raw.psls, "LS");
        emoji.put(R.raw.psrs, "RS");
        emoji.put(R.raw.pslsshang, "LS_上");
        emoji.put(R.raw.pslsyoushang, "LS_右上");
        emoji.put(R.raw.pslsyou, "LS_右");
        emoji.put(R.raw.pslsyouxia, "LS_右下");
        emoji.put(R.raw.pslsxia, "LS_下");
        emoji.put(R.raw.pslszuoxia, "LS_左下");
        emoji.put(R.raw.pslszuo, "LS_左");
        emoji.put(R.raw.pslszuoshang, "LS_左上");
        emoji.put(R.raw.psrsshang, "RS_上");
        emoji.put(R.raw.psrsyoushang, "RS_右上");
        emoji.put(R.raw.psrsyou, "RS_右");
        emoji.put(R.raw.psrsyouxia, "RS_右下");
        emoji.put(R.raw.psrsxia, "RS_下");
        emoji.put(R.raw.psrszuoxia, "RS_左下");
        emoji.put(R.raw.psrszuo, "RS_左");
        emoji.put(R.raw.psrszuoshang, "RS_左上");
        emoji.put(R.raw.aluhanxiao, "阿鲁憨笑");
        emoji.put(R.raw.aluzhoumei, "阿鲁皱眉");
        emoji.put(R.raw.alubukaixin, "阿鲁不开心");
        emoji.put(R.raw.aluyinxiao, "阿鲁阴笑");
        emoji.put(R.raw.aluchijing, "阿鲁吃惊");
        emoji.put(R.raw.alumengbi, "阿鲁懵逼");
        emoji.put(R.raw.aluweiqu, "阿鲁委屈");
        emoji.put(R.raw.alumangran, "阿鲁茫然");
        emoji.put(R.raw.aluxd, "阿鲁XD");
        emoji.put(R.raw.aluchongbai, "阿鲁崇拜");
        emoji.put(R.raw.aluyinxiao, "阿鲁淫笑");
        emoji.put(R.raw.aluliaoya, "阿鲁獠牙");
        emoji.put(R.raw.aluku, "阿鲁哭");
        emoji.put(R.raw.alumangmangran, "阿鲁茫茫然");
        emoji.put(R.raw.alulianhong, "阿鲁脸红");
        emoji.put(R.raw.aluqinqin, "阿鲁亲亲");
        emoji.put(R.raw.aluchuhan, "阿鲁出汗");
        emoji.put(R.raw.alukeshui, "阿鲁瞌睡");
        emoji.put(R.raw.alumojing, "阿鲁墨镜");
        emoji.put(R.raw.alukoubi, "阿鲁抠鼻");
        emoji.put(R.raw.aluchitang, "阿鲁吃糖");
        emoji.put(R.raw.aluchuxue, "阿鲁出血");
        emoji.put(R.raw.alukoushui, "阿鲁口水");
        emoji.put(R.raw.alutule, "阿鲁吐了");
        emoji.put(R.raw.alubiti, "阿鲁鼻涕");
        emoji.put(R.raw.alubengdai, "阿鲁绷带");
        emoji.put(R.raw.alutushe, "阿鲁吐舌");
        emoji.put(R.raw.alubizui, "阿鲁闭嘴");
        emoji.put(R.raw.alufujing, "阿鲁扶镜");
        emoji.put(R.raw.aludama, "阿鲁打码");
        emoji.put(R.raw.alutuxue, "阿鲁吐血");
        emoji.put(R.raw.alumaohuo, "阿鲁冒火");
        emoji.put(R.raw.aludongjie, "阿鲁冻结");
        emoji.put(R.raw.aluguale, "阿鲁挂了");
        emoji.put(R.raw.aludianzan, "阿鲁点赞");
        emoji.put(R.raw.aluyiyi, "阿鲁异议");
        emoji.put(R.raw.aluwunai, "阿鲁无奈");
        emoji.put(R.raw.alukaisen, "阿鲁开森");
        emoji.put(R.raw.aluwulian, "阿鲁捂脸");
        emoji.put(R.raw.aluhaixiu, "阿鲁害羞");
        emoji.put(R.raw.alulianteng, "阿鲁脸疼");
        emoji.put(R.raw.aluzuomo, "阿鲁琢磨");
        emoji.put(R.raw.aluguzhang, "阿鲁鼓掌");
        emoji.put(R.raw.aludoge, "阿鲁DOGE");
    }

    public static String getEmojiName(int id) {
        return emoji.get(id);
    }

    public static List<Integer> getEmojiID() {
        List<Integer> list = new ArrayList<>();
        for (Integer i : emoji.keySet()) {
            list.add(i);
        }
        return list;
    }
}
