package club.ranleng.psnine.adapter.PSNGame;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.Common.ArticleListAdapter;
import club.ranleng.psnine.model.Common.ArticleList;
import club.ranleng.psnine.model.PSNGame.PSNGameTrophy;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by ran on 09/07/2017.
 */

public class PSNGameTrophyAdapter extends ItemViewBinder<PSNGameTrophy, PSNGameTrophyAdapter.ViewHolder> {

    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public PSNGameTrophyAdapter (OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }

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
        holder.date.setText(item.date);
        holder.percent.setText(item.percent);
        holder.itemView.setTag(item.id);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.psngame_trophy_icon) ImageView icon;
        @BindView(R.id.psngame_trophy_name) TextView name;
        @BindView(R.id.psngame_trophy_des) TextView des;
        @BindView(R.id.psngame_trophy_date) TextView date;
        @BindView(R.id.psngame_trophy_percent) TextView percent;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onClick(itemView, getAdapterPosition());
            }
        }

    }
}
