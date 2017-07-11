package club.ranleng.psnine.adapter.Article;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.model.Article.ArticleGameList;
import club.ranleng.psnine.util.AndroidUtilCode.LogUtils;
import club.ranleng.psnine.util.AndroidUtilCode.ScreenUtils;
import me.drakeet.multitype.ItemViewBinder;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

/**
 * Created by ran on 02/07/2017.
 */

public class ArticleGameListAdapter  extends ItemViewBinder<ArticleGameList, ArticleGameListAdapter.ViewHolder> {

    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onGameClick(View view);
    }

    public ArticleGameListAdapter(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    protected ArticleGameListAdapter.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.adapter_gamelist_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ArticleGameListAdapter.ViewHolder holder, @NonNull ArticleGameList item) {
        holder.game_mode.setText(item.mode);
        holder.game_name.setText(item.name);
        holder.game_percent.setText(item.percent);
        Spanned spanned;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(item.trophy, FROM_HTML_MODE_LEGACY, null, null);
        } else {
            spanned = Html.fromHtml(item.trophy, null,null);
        }
        holder.game_trophy.setText(spanned);
        Glide.with(holder.itemView.getContext()).load(item.icon).into(holder.game_icon);
        holder.itemView.setTag(item.trophy_id);
        if(item.is_comment){
            holder.game_comment.setText(item.comment);
        }else{
            holder.game_comment.setVisibility(View.GONE);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
            if(clickListener != null){
                clickListener.onGameClick(itemView);
            }
        }
    }
}
