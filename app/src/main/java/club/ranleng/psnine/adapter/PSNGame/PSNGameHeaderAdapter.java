package club.ranleng.psnine.adapter.PSNGame;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.model.PSNGame.PSNGameHeader;
import club.ranleng.psnine.model.PSNGame.PSNGameTrophy;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by ran on 09/07/2017.
 */

public class PSNGameHeaderAdapter extends ItemViewBinder<PSNGameHeader, PSNGameHeaderAdapter.ViewHolder> {

    public PSNGameHeaderAdapter(){
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.adatper_psngame_header_item,null);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull PSNGameHeader item) {
        Glide.with(holder.itemView.getContext()).load(item.icon).into(holder.icon);
        holder.name.setText(item.name);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.psngame_header_icon) ImageView icon;
        @BindView(R.id.psngame_header_name) TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}