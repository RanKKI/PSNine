package club.ranleng.psnine.adapter;

import android.content.Context;
import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.model.ArticleReply;
import club.ranleng.psnine.widget.HtmlImageGetter;
import club.ranleng.psnine.widget.HtmlTagHandler;
import me.drakeet.multitype.ItemViewBinder;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

/**
 * Created by ran on 01/07/2017.
 */

public class ArticleReplyAdapter extends ItemViewBinder<ArticleReply, ArticleReplyAdapter.ViewHolder> {


    public ArticleReplyAdapter(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.adapter_reply_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ArticleReply item) {
        Map<String, Object> map = item.replies;

        Context context = holder.itemView.getContext();
        Spanned spanned;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spanned = Html.fromHtml((String) map.get("title"), FROM_HTML_MODE_LEGACY, new HtmlImageGetter(context, holder.title), new HtmlTagHandler(context));
        } else {
            spanned = Html.fromHtml((String) map.get("title"), new HtmlImageGetter(context, holder.title), new HtmlTagHandler(context));
        }
        holder.title.setText(spanned);
        holder.title.setSingleLine(false);
        holder.title.setMovementMethod(LinkMovementMethod.getInstance());

        holder.itemView.setTag(R.id.tag_article_replies_id, map.get("id"));
        holder.itemView.setTag(R.id.tag_article_replies_editable, map.get("editable"));
        holder.itemView.setTag(R.id.tag_article_replies_username, map.get("username"));
        holder.name.setText((String) map.get("username"));
        holder.time.setText((String) map.get("time"));
        Glide.with(holder.itemView.getContext()).load(map.get("icon")).into(holder.icon);

    }
    private OnItemClickListener clickListener;
    public interface OnItemClickListener {
        void onClick(View view,int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.adapter_list_title) TextView title;
        @BindView(R.id.adapter_list_name) TextView name;
        @BindView(R.id.adapter_list_time) TextView time;
        @BindView(R.id.adapter_list_icon) ImageView icon;
        @BindView(R.id.adapter_list_reply) TextView reply;
        @BindView(R.id.adapter_list_root) ImageView root;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            root.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onClick(itemView, getAdapterPosition());
            }
        }
    }
}
