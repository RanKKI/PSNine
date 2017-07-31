package club.ranleng.psnine.adapter.Binder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import club.ranleng.psnine.activity.FragActivity;
import club.ranleng.psnine.model.PSNGameTrophy;
import club.ranleng.psnine.widget.KEY;
import me.drakeet.multitype.ItemViewBinder;


public class PSNGameTrophyBinder extends ItemViewBinder<PSNGameTrophy, PSNGameTrophyBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.adatper_psngame_trophy_item,null);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull PSNGameTrophy item) {
        Glide.with(holder.itemView.getContext()).load(item.icon).into(holder.icon);
        holder.name.setText(item.name);
        holder.des.setText(item.des);
        if(item.date.contentEquals("")){
            holder.psngame_trophy_date_root.setVisibility(View.INVISIBLE);
        }else{
            holder.psngame_trophy_date_root.setVisibility(View.VISIBLE);
            holder.date.setText(item.date);
        }

        if(item.percent.contentEquals("")){
            holder.psngame_trophy_st_root.setVisibility(View.INVISIBLE);
        }else{
            holder.psngame_trophy_st_root.setVisibility(View.VISIBLE);
            holder.percent.setText(item.percent);
        }

        holder.itemView.setTag(item.id);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.psngame_trophy_icon) ImageView icon;
        @BindView(R.id.psngame_trophy_name) TextView name;
        @BindView(R.id.psngame_trophy_des) TextView des;
        @BindView(R.id.psngame_trophy_date) TextView date;
        @BindView(R.id.psngame_trophy_percent) TextView percent;
        @BindView(R.id.psngame_trophy_date_root) FrameLayout psngame_trophy_date_root;
        @BindView(R.id.psngame_trophy_st_root) FrameLayout psngame_trophy_st_root;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), FragActivity.class);
            intent.putExtra("key", KEY.TROPHYTIPS);
            intent.putExtra("trophy_id",v.getTag().toString());
            v.getContext().startActivity(intent);
        }

    }
}