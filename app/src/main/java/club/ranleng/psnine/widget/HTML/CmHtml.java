package club.ranleng.psnine.widget.HTML;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import club.ranleng.psnine.adapter.TextEditableItemAdapter;
import club.ranleng.psnine.adapter.TrophyAdapter;
import club.ranleng.psnine.model.TextSpannedItem;
import club.ranleng.psnine.model.Trophy;
import club.ranleng.psnine.util.LogUtils;
import club.ranleng.psnine.widget.ParseWebpage;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by ran on 05/07/2017.
 */

public class CmHtml {

    private static Context context;

    public static void init(Context c) {
        context = c;
    }

    public static void convert(TextView t, String s) {
        if(s.contains("blockquote")){
            s = s.replace("blockquote","a");
        }
        t.setText(returnHtml(s));
        t.invalidate();
        t.setText(t.getText()); // 解决图文重叠
        t.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private static Spanned returnHtml(String s){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY,new HtmlImageGetter(context), new HtmlTagHandler(context));
        }else{
            return Html.fromHtml(s,new HtmlImageGetter(context), new HtmlTagHandler(context));
        }
    }

    public static void convert(RecyclerView t, String s) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        t.setLayoutManager(mLayoutManager);
        MultiTypeAdapter adapter = new MultiTypeAdapter();
        Items items = new Items();

        adapter.register(Trophy.class, new TrophyAdapter());
        adapter.register(TextSpannedItem.class, new TextEditableItemAdapter());

        Document doc = Jsoup.parse(s);
        String key = "817857B0693710898411438234078B55";
        int trophy_num = 0;
        for(Element i : doc.select("div.pd10.t4")){
            Element a = i.before(key);
            Element b = i.after(key);
//            LogUtils.d(i.outerHtml());
            trophy_num ++;
        }

        for(Element i : doc.select("div.pd10.t3")){
            Element a = i.before(key);
            Element b = i.after(key);
            trophy_num ++;
        }

        if(!doc.toString().contains(key)){
            items.add(new TextSpannedItem(returnHtml(doc.toString())));
        }else {
            String[] trophy = doc.toString().split(key);
            if(trophy.length == trophy_num + 2){
                for (int i = 1; i < trophy.length + 1; i++) {
                    String content = trophy[i-1].replace("</trophy>","");
                    LogUtils.d(content);
                    if(i % 2 == 0){
                        items.add(new Trophy(ParseWebpage.parseTropy(content)));
                        LogUtils.d("奖杯");
                    }else{
                        LogUtils.d("不是奖杯");
                        items.add(new TextSpannedItem(returnHtml(content)));
                    }
                }
            }
        }

        adapter.setItems(items);
        t.setAdapter(adapter);
    }
}
