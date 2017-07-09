package club.ranleng.psnine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.Common.ArticleListAdapter;
import club.ranleng.psnine.model.GameList;
import me.drakeet.multitype.ItemViewBinder;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

/**
 * Created by ran on 01/07/2017.
 */

public class PSNGameListAdapter extends ItemViewBinder<GameList, PSNGameListAdapter.ViewHolder> {


    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public PSNGameListAdapter(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.adapter_personal_game_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull GameList item) {
        Context context = holder.itemView.getContext();
        holder.profile_game_title.setText(item.game_name);
        holder.profile_game_time.setText(item.last_time);
        holder.profile_game_difficulty.setText(item.difficulty);
        holder.profile_game_perfection.setText(item.perfection);
        Spanned spanned;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(item.trophy, FROM_HTML_MODE_LEGACY, null, null);
        } else {
            spanned = Html.fromHtml(item.trophy, null,null);
        }
        holder.profile_game_trophy.setText(spanned);
        holder.profile_game_percent.setProgress(item.progress);
        holder.itemView.setTag(R.id.tag_game_id,item.id);
        holder.itemView.setTag(R.id.tag_game_name,item.game_name);
        Glide.with(context).load(item.game_icon).into(holder.profile_game_img);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.profile_game_title) TextView profile_game_title;
        @BindView(R.id.profile_game_time) TextView profile_game_time;
        @BindView(R.id.profile_game_difficulty) TextView profile_game_difficulty;
        @BindView(R.id.profile_game_perfection) TextView profile_game_perfection;
        @BindView(R.id.profile_game_trophy) TextView profile_game_trophy;
        @BindView(R.id.profile_game_img) ImageView profile_game_img;
        @BindView(R.id.profile_game_percent) ProgressBar profile_game_percent;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(itemView, getAdapterPosition());
        }
    }
}
