package club.ranleng.psnine.common.multitype.binder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.common.multitype.model.ArticleHeader;
import club.ranleng.psnine.utils.HTML.cHtml;
import me.drakeet.multitype.ItemViewBinder;

public class ArticleHeaderBinder extends ItemViewBinder<ArticleHeader, ArticleHeaderBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_article_header, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ArticleHeader item) {

        if (holder.itemView.getTag() == null) {
            final Context context = holder.itemView.getContext();
            String content;
            if(item.title.equals("")){
                content = item.content;
            }else{
                content = "<h5>"+item.title+"</h5></br>" +  item.content;
            }
            cHtml.convert(context, holder.content, content);
            holder.time.setText(item.time);
            holder.replies.setText(item.replies);
            holder.username.setText(item.username);
            Glide.with(context).load(item.icon).into(holder.icon);
            holder.itemView.setTag("");
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.header_content) RecyclerView content;
        @BindView(R.id.header_time) TextView time;
        @BindView(R.id.header_replies) TextView replies;
        @BindView(R.id.header_username) TextView username;
        @BindView(R.id.header_icon) ImageView icon;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
