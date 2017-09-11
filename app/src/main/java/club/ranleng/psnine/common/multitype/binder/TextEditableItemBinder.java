package club.ranleng.psnine.common.multitype.binder;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import club.ranleng.psnine.common.multitype.model.TextSpannedItem;
import club.ranleng.psnine.utils.HTML.cHtml;
import me.drakeet.multitype.ItemViewBinder;


public class TextEditableItemBinder extends ItemViewBinder<TextSpannedItem, TextEditableItemBinder.ViewHolder> {

    @NonNull
    @Override
    protected TextEditableItemBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        TextView textview = new TextView(inflater.getContext().getApplicationContext());
        textview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(textview);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull TextSpannedItem item) {
        TextView text = (TextView) holder.itemView;
        if (item.getMid()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            } else {
                text.setGravity(Gravity.CENTER);
            }
        }
        text.setText(cHtml.returnHtml(holder.itemView.getContext(), text, item.getText()));
        text.setTextSize(item.getSize());
        text.setPadding(item.getPadding(), item.getPadding(), item.getPadding(), item.getPadding());
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }

    }
}
