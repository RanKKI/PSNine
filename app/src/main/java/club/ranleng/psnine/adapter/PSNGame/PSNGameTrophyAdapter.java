package club.ranleng.psnine.adapter.PSNGame;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
