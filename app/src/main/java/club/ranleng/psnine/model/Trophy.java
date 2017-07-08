package club.ranleng.psnine.model;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

import butterknife.BindView;
import club.ranleng.psnine.R;

/**
 * Created by ran on 06/07/2017.
 */

public class Trophy{

    public final String game_icon_url;
    public final String game_name;
    public final String game_des;
    public final String user_name;
    public final String user_comment;
    public final String time;
    public final String has_comment;

    public Trophy(Map<String, String> map) {
        this.game_icon_url = map.get("game_icon_url");
        this.game_name = map.get("game_name");
        this.game_des = map.get("game_des");
        this.user_name= map.get("user_name");
        this.user_comment = map.get("user_comment");
        this.time = map.get("time");
        this.has_comment = map.get("has_comment");

    }



}
