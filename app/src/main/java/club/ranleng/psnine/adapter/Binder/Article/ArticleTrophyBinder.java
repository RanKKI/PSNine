package club.ranleng.psnine.adapter.Binder.Article;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.FragActivity;
import club.ranleng.psnine.model.Article.ArticleTrophy;
import club.ranleng.psnine.widget.HTML.CmHtml;
import club.ranleng.psnine.widget.KEY;
import me.drakeet.multitype.ItemViewBinder;


public class ArticleTrophyBinder extends ItemViewBinder<ArticleTrophy, ArticleTrophyBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.adapter_trophy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final ArticleTrophy item) {
        final Context context = holder.itemView.getContext();
        Glide.with(context).load(item.game_icon_url).into(holder.game_icon);
        holder.itemView.setTag(R.id.TAG_ARTICLE_LIST_TROPHY_ID,item.trophy_id);
        holder.game_name.setText(item.game_name);
        holder.game_des.setText(item.game_des);
        if (item.has_comment.contentEquals("true")) {
            holder.user_name.setText(item.user_name);
            holder.user_comment.setText(CmHtml.returnHtml(context, item.user_comment));
            holder.time.setText(item.time);
        } else {
            holder.root.setVisibility(View.GONE);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.view_trophy_icon) ImageView game_icon;
        @BindView(R.id.view_trophy_name) TextView game_name;
        @BindView(R.id.view_trophy_des) TextView game_des;
        @BindView(R.id.view_trophy_user_name) TextView user_name;
        @BindView(R.id.view_trophy_user_comment) TextView user_comment;
        @BindView(R.id.view_trophy_user_time) TextView time;
        @BindView(R.id.view_trophy_comment_root) RelativeLayout root;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent intent = new Intent(context, FragActivity.class);
            intent.putExtra("key", KEY.TROPHYTIPS);
            intent.putExtra("trophy_id", (String) v.getTag(R.id.TAG_ARTICLE_LIST_TROPHY_ID));
            context.startActivity(intent);
        }
    }
}
