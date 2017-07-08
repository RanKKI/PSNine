package club.ranleng.psnine.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.model.TextSpannedItem;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by ran on 02/07/2017.
 */

public class TextEditableItemAdapter extends ItemViewBinder<TextSpannedItem, TextEditableItemAdapter.ViewHolder> {

    @NonNull
    @Override
    protected TextEditableItemAdapter.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.adapter_text_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull TextEditableItemAdapter.ViewHolder holder, @NonNull TextSpannedItem item) {
        holder.text.setText(item.text);
//        holder.text.setTextColor(Color.parseColor(item.color));
        holder.text.setTextSize(14);
        holder.text.invalidate();
        holder.text.setText(holder.text.getText()); // 解决图文重叠
        holder.text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.textView) TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
