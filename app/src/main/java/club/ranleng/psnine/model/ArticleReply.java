package club.ranleng.psnine.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ran on 01/07/2017.
 */

public class ArticleReply {

    public @NonNull Map<String, Object> replies;

    public ArticleReply(@NonNull Map<String, Object> replies){
        this.replies = replies;
    }
}
