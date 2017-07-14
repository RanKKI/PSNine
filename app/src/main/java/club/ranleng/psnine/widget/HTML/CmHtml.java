package club.ranleng.psnine.widget.HTML;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import club.ranleng.psnine.adapter.ViewBinder.Article.TrophyBinder;
import club.ranleng.psnine.adapter.ViewBinder.Common.TableBinder;
import club.ranleng.psnine.adapter.ViewBinder.TextEditableItemBinder;
import club.ranleng.psnine.model.Common.Table;
import club.ranleng.psnine.model.TextSpannedItem;
import club.ranleng.psnine.model.Article.Trophy;
import club.ranleng.psnine.util.AndroidUtilCode.LogUtils;
import club.ranleng.psnine.widget.ParseWebpage;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by ran on 05/07/2017.
 */

public class CmHtml {

    private static String[] trophy_class = {"div.pd10.t1","div.pd10.t2","div.pd10.t3","div.pd10.t4","table.tbl"};

    public static Spanned returnHtml(Context c,String s){
        return mHtml.fromHtml(s,new HtmlImageGetter(c),new HtmlTagHandler(c));
    }

    public static Spanned returnHtml(Context c,TextView t, String s){
        return mHtml.fromHtml(s,new HtmlImageGetter(c,t),new HtmlTagHandler(c));
    }

    public static void convert(Context c,RecyclerView t, String s) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(c,
                LinearLayoutManager.VERTICAL, false);
        t.setLayoutManager(mLayoutManager);
        MultiTypeAdapter adapter = new MultiTypeAdapter();
        Items items = new Items();

        adapter.register(Table.class, new TableBinder());
        adapter.register(Trophy.class, new TrophyBinder());
        adapter.register(TextSpannedItem.class, new TextEditableItemBinder());

        Document doc = Jsoup.parse(s);
        String key = "817857B0693710898411438234078B55";
        for (String d: trophy_class) {
            for(Element i : doc.select(d)){
                Element a = i.before(key);
                Element b = i.after(key);
            }
        }

        if(!doc.toString().contains(key)){
            items.add(new TextSpannedItem(doc.toString()));
        }else {
            String[] trophy = doc.toString().split(key);
            for (int i = 0; i < trophy.length; i++) {
                String content = trophy[i];
                LogUtils.d(content);
                if(i % 2 == 1){
                    if(content.contains("tbody")){
                        items.add(new Table(ParseWebpage.parseTable(content)));
                    }else{
                        items.add(new Trophy(ParseWebpage.parseTropy(content)));
                    }
                }else{
                    items.add(new TextSpannedItem(content));
                }
            }
        }

        adapter.setItems(items);
        t.setAdapter(adapter);
    }
}
