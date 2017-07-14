package club.ranleng.psnine.adapter.ViewBinder;

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
import club.ranleng.psnine.model.GameList;
import club.ranleng.psnine.model.PSNGames;
import club.ranleng.psnine.widget.HTML.CmHtml;
import me.drakeet.multitype.ItemViewBinder;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

/**
 * Created by ran on 01/07/2017.
 */

public class PSNGamesListBinder extends ItemViewBinder<PSNGames, PSNGamesListBinder.ViewHolder> {


    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public PSNGamesListBinder(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.view_psn_games_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull PSNGames item) {
        Context context = holder.itemView.getContext();
        holder.profile_game_title.setText(CmHtml.returnHtml(context,item.name));
        holder.profile_game_difficulty.setText(item.mode);
        holder.profile_game_perfection.setText(item.perfect);
        holder.profile_game_trophy.setText(CmHtml.returnHtml(context,item.trophy));
        holder.itemView.setTag(R.id.tag_game_id,item.id);
        Glide.with(context).load(item.icon).into(holder.profile_game_img);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.profile_game_title) TextView profile_game_title;
        @BindView(R.id.profile_game_difficulty) TextView profile_game_difficulty;
        @BindView(R.id.profile_game_perfection) TextView profile_game_perfection;
        @BindView(R.id.profile_game_trophy) TextView profile_game_trophy;
        @BindView(R.id.profile_game_img) ImageView profile_game_img;

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
