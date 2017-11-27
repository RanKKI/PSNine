package club.ranleng.psnine.model;

import android.text.Html;
import android.text.Spanned;

import me.ghui.fruit.Attrs;
import me.ghui.fruit.annotations.Pick;

public class PSNUserInfo {

    @Pick(value = "div > div.psnava > img.avabig", attr = Attrs.SRC)
    private String avatar;
    @Pick(value = "div > div.psnava > img.icon-region", attr = Attrs.SRC)
    private String region;
    @Pick(value = "div > div.psnava > img.icon-auth", attr = Attrs.SRC)
    private String auth;
    @Pick(value = "div > div.psnava > img.icon-plus", attr = Attrs.SRC)
    private String plus;
    @Pick(value = "body > div:nth-child(2) > div > div > div.psntrophy", attr = Attrs.INNER_HTML)
    private String trophy;
    @Pick(value = "div:nth-child(1) > table > tbody > tr > td:nth-child(1) > em")
    private String des;

    public String getAvatar() {
        return avatar;
    }

    public Spanned getTrophy() {
        return Html.fromHtml(trophy);
    }

    public String getRegion() {
        return region;
    }

    public String getAuth() {
        return auth;
    }

    public String getPlus() {
        return plus;
    }

    public String getDes() {
        return des;
    }
}
