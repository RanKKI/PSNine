package club.ranleng.psnine.common.multitype.binder;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import club.ranleng.psnine.common.multitype.model.MutilPages;
import me.drakeet.multitype.ItemViewBinder;

public class MutilPagesBinder extends ItemViewBinder<MutilPages, MutilPagesBinder.ViewHolder> {

    private OnPageChange onPageChange;
    private String[] list_name;

    public MutilPagesBinder(OnPageChange onPageChange) {
        this.onPageChange = onPageChange;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        Button button = new Button(inflater.getContext().getApplicationContext());
        button.setBackgroundColor(0xFF3498DB);
        button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        button.setTextColor(0xFFF1F1F1);
        return new ViewHolder(button);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MutilPages item) {
        list_name = item.pages.toArray(new String[0]);
        ((Button) holder.itemView).setText(item.pages.get(0));
    }

    public interface OnPageChange {
        void onpagechage(int page);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            AlertDialog b = new AlertDialog.Builder(itemView.getContext()).setItems(list_name, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((Button) v).setText(list_name[which]);
                    if (onPageChange != null) {
                        onPageChange.onpagechage(which + 1);
                    }
                }
            }).create();
            b.show();
        }
    }
}
