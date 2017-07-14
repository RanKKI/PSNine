package club.ranleng.psnine.adapter.ViewBinder.Common;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.model.Article.MutilPages;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by ran on 10/07/2017.
 */

public class MutilPagesBinder extends ItemViewBinder<MutilPages, MutilPagesBinder.ViewHolder> {

    private OnPageChange onPageChange;
    private String[] list_name;

    public interface OnPageChange{
        void onpagechage(int page);
    }

    public MutilPagesBinder(OnPageChange onPageChange){
        this.onPageChange = onPageChange;
    }


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.adapter_mutil_pages,null);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MutilPages item) {
        list_name = item.pages.toArray(new String[0]);
        holder.root.setText(item.pages.get(0));
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.mutil_pages_button) Button root;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            root.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            AlertDialog b = new AlertDialog.Builder(itemView.getContext()).setItems(list_name, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    root.setText(list_name[which]);
                    if(onPageChange!=null){
                        onPageChange.onpagechage(which + 1);
                    }
                }
            }).create();
            b.show();
        }
    }
}
