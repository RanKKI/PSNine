package club.ranleng.psnine.common.multitype.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import club.ranleng.psnine.R;
import club.ranleng.psnine.common.multitype.model.CardBean;
import club.ranleng.psnine.utils.FeedBackUtils;
import me.drakeet.multitype.ItemViewBinder;

public class CardViewBinder extends ItemViewBinder<CardBean, CardViewBinder.ViewHolder> {

    private View.OnClickListener onClickListener;

    public CardViewBinder(View.OnClickListener listener) {
        this.onClickListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.view_card, parent, false);
        return new ViewHolder(root);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull CardBean cardBean) {
        holder.content.setText(cardBean.getContent());
        holder.itemView.setTag(cardBean.getContent());
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView content;

        ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        }
    }
}
