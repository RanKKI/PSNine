package club.ranleng.psnine.ui.psn;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.model.TransInfo;

public class PSNAccountDetailTransInfoAdapter extends RecyclerView.Adapter<PSNAccountDetailTransInfoAdapter.ViewHolder> {

    private List<TransInfo.Item> items = new ArrayList<>();

    public void add(TransInfo transInfo) {
        items.addAll(transInfo.getItems());
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(Utils.getApp()).inflate(R.layout.adapter_transinfo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TransInfo.Item item = items.get(position);
        holder.amount.setText(item.getAmount());
        holder.type.setText(item.getType());
        holder.transNum.setText(item.getTransNum());
        holder.date.setText(item.getDate());
        holder.status.setText(item.getStatus());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.amount) TextView amount;
        @BindView(R.id.type) TextView type;
        @BindView(R.id.transNum) TextView transNum;
        @BindView(R.id.date) TextView date;
        @BindView(R.id.status) TextView status;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
