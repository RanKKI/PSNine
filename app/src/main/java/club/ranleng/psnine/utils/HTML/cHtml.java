package club.ranleng.psnine.utils.HTML;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import club.ranleng.psnine.common.multitype.binder.ArticleTrophyBinder;
import club.ranleng.psnine.common.multitype.binder.TableBinder;
import club.ranleng.psnine.common.multitype.binder.TextEditableItemBinder;
import club.ranleng.psnine.common.multitype.model.ArticleTrophy;
import club.ranleng.psnine.common.multitype.model.Table;
import club.ranleng.psnine.common.multitype.model.TextSpannedItem;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class cHtml {
    private static String[] trophy_class = {"div.pd10.t1", "div.pd10.t2", "div.pd10.t3", "div.pd10.t4", "table.tbl"};

    public static Spanned returnHtml(Context context, String source) {
        return mHtml.fromHtml(source, new HtmlImageGetter(context), new HtmlTagHandler(context));
    }

    public static Spanned returnHtml(Context context, TextView tv, String source) {
        return mHtml.fromHtml(source, new HtmlImageGetter(context, tv), new HtmlTagHandler(context));
    }

    public static void convert(Context c, RecyclerView t, String s) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(c,
                LinearLayoutManager.VERTICAL, false);
        t.setLayoutManager(mLayoutManager);
        MultiTypeAdapter adapter = new MultiTypeAdapter();
        Items items = new Items();

        adapter.register(Table.class, new TableBinder());
        adapter.register(ArticleTrophy.class, new ArticleTrophyBinder());
        adapter.register(TextSpannedItem.class, new TextEditableItemBinder());

        Document doc = Jsoup.parse(s);
        String key = "817857B0693710898411438234078B55";
        for (String d : trophy_class) {
            for (Element i : doc.select(d)) {
                Element a = i.before(key);
                Element b = i.after(key);
            }
        }

        if (!doc.toString().contains(key)) {
            items.add(new TextSpannedItem(doc.toString()));
        } else {
            String[] trophy = doc.toString().split(key);
            for (int i = 0; i < trophy.length; i++) {
                String content = trophy[i];
                if (i % 2 == 1) {
                    if (content.contains("tbody")) {
                        items.add(new Table(ConvertHtml.parseTable(content)));
                    } else {
                        items.add(new ArticleTrophy(ConvertHtml.parseTropy(content)));
                    }
                } else {
                    items.add(new TextSpannedItem(content));
                }
            }
        }

        adapter.setItems(items);
        t.setAdapter(adapter);
    }
}
