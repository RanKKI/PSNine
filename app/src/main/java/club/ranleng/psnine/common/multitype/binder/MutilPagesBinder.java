package club.ranleng.psnine.common.multitype.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.blankj.utilcode.util.LogUtils;

import club.ranleng.psnine.common.multitype.model.MutilPages;
import me.drakeet.multitype.ItemViewBinder;

public class MutilPagesBinder extends ItemViewBinder<MutilPages, MutilPagesBinder.ViewHolder> {

    private OnClickListener onClickListener;
    private String[] list_name;

    public MutilPagesBinder(OnClickListener clickListener) {
        this.onClickListener = clickListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        Button button = new Button(inflater.getContext().getApplicationContext());
        button.setLayoutParams(new ViewGroup.LayoutParams(parent.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setPadding(0, 0, 0, 5);
        button.setBackgroundColor(0xFF3498DB);
        button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        button.setTextColor(0xFFF1F1F1);
        return new ViewHolder(button);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MutilPages item) {
        list_name = item.getPages().toArray(new String[0]);
        ((Button) holder.itemView).setText(list_name[item.getCurrent_page()]);
    }

    public interface OnClickListener {
        void OnClick(String[] topics);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            onClickListener.OnClick(list_name);
        }
    }
}
