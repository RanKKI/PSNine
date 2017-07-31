package club.ranleng.psnine.fragment.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.blankj.utilcode.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import club.ranleng.psnine.Listener.EditTabsItemsCallback;
import club.ranleng.psnine.adapter.EditTabsAdapter;
import club.ranleng.psnine.event.LoadEvent;
import club.ranleng.psnine.utils.MakeToast;
import club.ranleng.psnine.widget.KEY;


public class EditTabsDialogFragment extends DialogFragment {

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

        final List<Integer> list = KEY.getTabs();
        builder.setView(recyclerView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!list.equals(adapter.getDatas())){
                            KEY.setTabs(adapter.getDatas());
                            MakeToast.str("更新tabs后需要重启应用.....（暂时的");
                        }

                    }
                })
                .setNegativeButton("取消", null);

        return builder.create();
    }
}