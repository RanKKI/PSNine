package club.ranleng.psnine.common.multitype.binder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.common.multitype.model.ArticleGameList;
import me.drakeet.multitype.ItemViewBinder;

public class ArticleGameListBinder extends ItemViewBinder<ArticleGameList, ArticleGameListBinder.ViewHolder> {

    @NonNull
    @Override
    protected ArticleGameListBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_game_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ArticleGameListBinder.ViewHolder holder, @NonNull ArticleGameList item) {
        holder.game_mode.setText(item.mode);
        holder.game_name.setText(item.name);
        holder.game_percent.setText(item.percent);
        Spanned spanned;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(item.trophy, Html.FROM_HTML_MODE_LEGACY, null, null);
        } else {
            spanned = Html.fromHtml(item.trophy, null, null);
        }
        holder.game_trophy.setText(spanned);
        holder.itemView.setTag(R.id.TAG_GAME_TROPHY_ID, item.trophy_id);
        Glide.with(holder.itemView.getContext()).load(item.icon).into(holder.game_icon);
        holder.itemView.setTag(item.trophy_id);
        if (item.is_comment) {
            holder.game_comment.setText(item.comment);
        } else {
            holder.game_comment.setVisibility(View.GONE);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.game_mode) TextView game_mode;
        @BindView(R.id.game_name) TextView game_name;
        @BindView(R.id.game_percent) TextView game_percent;
        @BindView(R.id.game_trophy) TextView game_trophy;
        @BindView(R.id.game_icon) ImageView game_icon;
        @BindView(R.id.game_comment) TextView game_comment;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Context context = v.getContext();
//            Intent intent = new Intent(context, FragActivity.class);
//            intent.putExtra("key", KEY.TROPHY);
//            intent.putExtra("game_id", (String) v.getTag(R.id.TAG_GAME_TROPHY_IDD));
//            context.startActivity(intent);
        }
    }
}
