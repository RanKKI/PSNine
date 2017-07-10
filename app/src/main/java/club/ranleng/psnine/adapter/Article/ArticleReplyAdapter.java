package club.ranleng.psnine.adapter.Article;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.model.Article.ArticleReply;
import club.ranleng.psnine.widget.HTML.CmHtml;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by ran on 01/07/2017.
 */

public class ArticleReplyAdapter extends ItemViewBinder<ArticleReply, ArticleReplyAdapter.ViewHolder> {



    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.adapter_reply_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ArticleReply item) {
        Map<String, Object> map = item.replies;

        CmHtml.convert(holder.itemView.getContext(),holder.title,(String) map.get("title"));
        holder.itemView.setTag(R.id.tag_article_replies_id, map.get("id"));
        holder.itemView.setTag(R.id.tag_article_replies_editable, map.get("editable"));
        holder.itemView.setTag(R.id.tag_article_replies_username, map.get("username"));
        if((Boolean) map.get("editable")){
            holder.itemView.setTag(R.id.tag_article_replies_content, map.get("title"));
        }
        holder.name.setText((String) map.get("username"));
        holder.time.setText((String) map.get("time"));
        Glide.with(holder.itemView.getContext()).load(map.get("icon")).into(holder.icon);

    }
    private OnItemClickListener clickListener;
    public interface OnItemClickListener {
        void onClick(View root);
    }
    public ArticleReplyAdapter(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.adapter_list_title) RecyclerView title;
        @BindView(R.id.adapter_list_name) TextView name;
        @BindView(R.id.adapter_list_time) TextView time;
        @BindView(R.id.adapter_list_icon) ImageView icon;
        @BindView(R.id.adapter_list_reply) TextView reply;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onClick(itemView);
            }
        }
    }
}
