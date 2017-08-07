package club.ranleng.psnine.module.setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;

import java.util.Collections;
import java.util.List;

import club.ranleng.psnine.R;
import club.ranleng.psnine.common.KEY;
import club.ranleng.psnine.common.RxBus;
import club.ranleng.psnine.common.event.TabsChange;

public class TabsPreference extends DialogFragment {

    public static TabsPreference newInstance() {
        return new TabsPreference();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        RecyclerView recyclerView = new RecyclerView(Utils.getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        final EditTabsAdapter adapter = new EditTabsAdapter();

        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new EditTabsItemsCallback(adapter);
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);

        builder.setTitle(getString(R.string.long_press_to_move))
                .setView(recyclerView)
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!KEY.getTabs().equals(adapter.getDatas())) {
                            KEY.setTabs(adapter.getDatas());
                            RxBus.getDefault().send(new TabsChange(true));
                        }

                    }
                })
                .setNegativeButton(getString(R.string.cancel), null);

        return builder.create();
    }
}

class EditTabsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements EditTabsItemsCallback.onMoveAndSwipedListener {

    private List<Integer> mDatas;

    public EditTabsAdapter() {
        this.mDatas = KEY.getTabs();
    }

    public List<Integer> getDatas() {
        return mDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext().getApplicationContext());
        textView.setPadding(16, 16, 32, 16);
        textView.setTextSize(18);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        parent.addView(textView);
        return new RecyclerView.ViewHolder(textView) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((TextView) holder.itemView).setText(KEY.getTypeNameCN(mDatas.get(position)));
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    holder.itemView.setBackgroundColor(0x7FB2B2B2);
                } else {
                    holder.itemView.setBackgroundColor(0);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mDatas, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

}

class EditTabsItemsCallback extends ItemTouchHelper.Callback {

    private onMoveAndSwipedListener listener;

    public EditTabsItemsCallback(onMoveAndSwipedListener listener) {
        this.listener = listener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //如果是ListView样式的RecyclerView
        //设置拖拽方向为上下
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        //将方向参数设置进去
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setBackgroundColor(0x7F7A7A7A);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(0);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    public interface onMoveAndSwipedListener {
        void onItemMove(int fromPosition, int toPosition);
    }
}