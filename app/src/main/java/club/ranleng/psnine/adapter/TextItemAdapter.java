package club.ranleng.psnine.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.model.TextItem;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by ran on 02/07/2017.
 */

public class TextItemAdapter extends ItemViewBinder<TextItem, TextItemAdapter.ViewHolder> {

    @NonNull
    @Override
    protected TextItemAdapter.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.adapter_text_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull TextItemAdapter.ViewHolder holder, @NonNull TextItem item) {
        holder.text.setText(item.text);
        holder.text.setTextColor(Color.parseColor(item.color));
        holder.text.setTextSize(item.size);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.textView) TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
