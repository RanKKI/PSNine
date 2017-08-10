package club.ranleng.psnine.common.multitype.binder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import club.ranleng.psnine.common.multitype.model.GameList;
import club.ranleng.psnine.utils.HTML.cHtml;
import me.drakeet.multitype.ItemViewBinder;

public class PSNGameListBinder extends ItemViewBinder<GameList, PSNGameListBinder.ViewHolder> {

    private String psnid;

    public PSNGameListBinder(String psnid) {
        this.psnid = psnid;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.adapter_psngame_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull GameList item) {
        Context context = holder.itemView.getContext();
        holder.profile_game_title.setText(item.game_name);
        holder.profile_game_time.setText(item.last_time);
        holder.profile_game_difficulty.setText(item.difficulty);
        holder.profile_game_perfection.setText(item.perfection);
        holder.profile_game_trophy.setText(cHtml.returnHtml(context, item.trophy));
        holder.profile_game_percent.setProgress(item.progress);
        holder.itemView.setTag(R.id.TAG_GAME_ID, item.id);
        Glide.with(context).load(item.game_icon).into(holder.profile_game_img);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            Context context = v.getContext();
//            Intent intent = new Intent(context, FragActivity.class);
//            intent.putExtra("key", KEY.TROPHY);
//            intent.putExtra("game_id", (String) v.getTag(R.id.TAG_GAME_ID));
//            intent.putExtra("username",psnid);
//            context.startActivity(intent);
        }
    }
}
