package club.ranleng.psnine.common.multitype.binder;

import android.content.Context;
import android.os.Build;
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
        final Context context = holder.itemView.getContext();
        String content;
        if (item.getTitle().equals("")) {
            content = item.getContent();
        } else {
            content = "<h5>" + item.getTitle() + "</h5></br>" + item.getContent();
        }
        cHtml.convert(context, holder.content, content);
        holder.time.setText(item.getTime());
        holder.replies.setText(item.getReplies());
        holder.username.setText(item.getUsername());
        Glide.with(context).load(item.getIcon()).into(holder.icon);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.icon.setTransitionName(context.getString(R.string.trans_user_icon));
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
