package club.ranleng.psnine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import club.ranleng.psnine.Listener.EditTabsItemsCallback;
import club.ranleng.psnine.R;
import club.ranleng.psnine.widget.KEY;

public class EditTabsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements EditTabsItemsCallback.onMoveAndSwipedListener {

    private List<Integer> mDatas;

    public EditTabsAdapter() {
        this.mDatas = KEY.getTabs();
        KEY.setTabs(mDatas);
    }

    public List<Integer> getDatas() {
        return mDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_tabs_textview, parent, false);
        return new RecyclerView.ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((TextView) holder.itemView).setText(KEY.TYPE_NAME_CN.get(mDatas.get(position)));
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    holder.itemView.setBackgroundColor(0x7FB2B2B2);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    holder.itemView.setBackgroundColor(0);
                }
                return false;
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
