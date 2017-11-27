package club.ranleng.psnine.ui.topics.psngame;

import android.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.model.PSNGames;

public class TopicsPSNGameListAdapter extends RecyclerView.Adapter<TopicsPSNGameListAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private Fragment fragment;
    private List<PSNGames.Item> items = new ArrayList<>();

    public TopicsPSNGameListAdapter(Fragment fragment) {
        this.fragment = fragment;
        mLayoutInflater = LayoutInflater.from(Utils.getApp());
    }

    void add(final List<PSNGames.Item> newItems, int page) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return items.size();
            }

            @Override
            public int getNewListSize() {
                return newItems.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return true;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return items.get(oldItemPosition).getName().equals(newItems.get(newItemPosition).getName());
            }
        });
        if (page == 1) {
            items = newItems;
        } else {
            items.addAll(newItems);
        }
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.adapter_topics_psngame_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PSNGames.Item item = items.get(position);
        holder.name.setText(item.getName());
        holder.time.setText(item.getTime());
        holder.trophy.setText(item.getTrophy());
        holder.progressBar.setProgress(item.getProgress());
        Glide.with(fragment).load(item.getIcon()).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.PSNGameItemIcon) ImageView icon;
        @BindView(R.id.PSNGameItemName) TextView name;
        @BindView(R.id.PSNGameItemTime) TextView time;
        @BindView(R.id.PSNGameItemTrophy) TextView trophy;
        @BindView(R.id.PSNGameItemProgress) ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
