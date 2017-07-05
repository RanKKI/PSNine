package club.ranleng.psnine.widget.HTML;

import android.content.Context;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * Created by ran on 05/07/2017.
 */

public class CmHtml {

    private static Context context;

    public CmHtml(){

    }
    public static void init(Context c){
        context = c;
    }
    public static void convert(TextView t, String s){
        Spanned spanned = mHtml.fromHtml(s, new HtmlImageGetter(context, t), new HtmlTagHandler(context));
        t.setText(spanned);
        t.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
