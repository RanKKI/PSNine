package club.ranleng.psnine.model.Topic;

import android.text.Html;
import android.text.Spanned;

import me.ghui.fruit.Attrs;
import me.ghui.fruit.annotations.Pick;

public class TopicGame {

    @Pick(value = "td.pd10 > p > a")
    private String name;

    @Pick(value = "td.pdd10 > a > img", attr = Attrs.SRC)
    private String icon;

    @Pick(value = "td.pd10 > div")
    private String trophy;

    @Pick(value = "td.twoge.h-p > em")
    private String perfect;

    @Pick(value = "td.twoge.h-p > span")
    private String mode;

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public Spanned getTrophy() {
        return Html.fromHtml(trophy);
    }

    public int getPerfect(){
        Double d = Double.valueOf(perfect.replace(" ","").split("%")[0]);
        return d.intValue();
    }

    public String getMode() {
        return mode;
    }
}
