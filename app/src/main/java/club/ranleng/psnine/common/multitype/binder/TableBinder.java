package club.ranleng.psnine.common.multitype.binder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.common.multitype.model.Table;
import club.ranleng.psnine.utils.HTML.cHtml;
import me.drakeet.multitype.ItemViewBinder;


public class TableBinder extends ItemViewBinder<Table, TableBinder.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.adapter_table, null);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Table item) {
        Context ctx = holder.itemView.getContext();
        for (ArrayList<String> data : item.data) {
            TableRow tableRow = (TableRow) LayoutInflater
                    .from(holder.itemView.getContext()).inflate(R.layout.adapter_table_row_item, null)
                    .findViewById(R.id.adapter_table_row);
            TableLayout.LayoutParams param = new TableLayout.LayoutParams();
            param.setMargins(1, 1, 1, 1);
            tableRow.setLayoutParams(param);

            for (String str_data : data) {
                TableRow.LayoutParams params = new TableRow.LayoutParams();
                params.setMargins(1, 1, 1, 1);
                TextView textView = (TextView) LayoutInflater
                        .from(holder.itemView.getContext()).inflate(R.layout.adapter_table_item, null)
                        .findViewById(R.id.adapter_table_item);
                textView.setLayoutParams(params);
                textView.setText(cHtml.returnHtml(ctx, textView, str_data));
                textView.setBackgroundColor(0xFFFFFFFF);
                tableRow.addView(textView);
            }
            holder.root.addView(tableRow);
        }
        holder.root.setBackgroundColor(0xFF000000);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.table_root) TableLayout root;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
