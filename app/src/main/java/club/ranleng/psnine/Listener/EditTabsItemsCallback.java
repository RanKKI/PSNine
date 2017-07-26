package club.ranleng.psnine.Listener;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class EditTabsItemsCallback extends ItemTouchHelper.Callback {

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
