package club.ranleng.psnine.adapter.Article;

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
import club.ranleng.psnine.activity.Main.GameTrophyTipsActivity;
import club.ranleng.psnine.model.Article.Trophy;
import club.ranleng.psnine.widget.HTML.CmHtml;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by ran on 01/07/2017.
 */

public class TrophyAdapter extends ItemViewBinder<Trophy, TrophyAdapter.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.adapter_trophy,parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Trophy item) {
        final Context context = holder.itemView.getContext();
        Glide.with(context).load(item.game_icon_url).into(holder.game_icon);
        holder.game_name.setText(item.game_name);
        holder.game_des.setText(item.game_des);
        if(item.has_comment.contentEquals("true")){
            holder.user_name.setText(item.user_name);
            holder.user_comment.setText(CmHtml.returnHtml(context,item.user_comment));
            holder.time.setText(item.time);
        }else{
            holder.root.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GameTrophyTipsActivity.class);
                intent.putExtra("trophy_id",item.trophy_id);
                context.startActivity(intent);
            }
        });

    }

    class ViewHolder extends RecyclerView.ViewHolder{

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
        }

    }
}
