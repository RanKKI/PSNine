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
import org.jsoup.select.Elements;

import java.util.ArrayList;

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
    private static String[] trophy_class = {"div.pd10.t1","div.pd10.t2","div.pd10.t3","div.pd10.t4"};

    public static void init(Context c) {
        context = c;
    }

    public static void convert(Context c,TextView t, String s) {
        if(s.contains("blockquote")){
            s = s.replace("blockquote","a");
        }
        t.setText(returnHtml(c,s));
        t.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static Spanned returnHtml(Context c,String s){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY,new HtmlImageGetter(c), new HtmlTagHandler(c));
        }else{
            return Html.fromHtml(s,new HtmlImageGetter(context), new HtmlTagHandler(c));
        }
    }

    public static void convert(Context c,RecyclerView t, String s) {
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
        for (String d: trophy_class) {
            for(Element i : doc.select(d)){
                Element a = i.before(key);
                Element b = i.after(key);
                trophy_num ++;
            }
        }

        if(!doc.toString().contains(key)){
            items.add(new TextSpannedItem(returnHtml(c,doc.toString())));
        }else {
            String[] trophy = doc.toString().split(key);
            if(trophy.length == trophy_num + 2){
                for (int i = 1; i < trophy.length + 1; i++) {
                    String content = trophy[i-1];
                    if(i % 2 == 0){
                        items.add(new Trophy(ParseWebpage.parseTropy(content)));
                    }else{
                        items.add(new TextSpannedItem(returnHtml(c,content)));
                    }
                }
            }
        }

        adapter.setItems(items);
        t.setAdapter(adapter);
    }
}
