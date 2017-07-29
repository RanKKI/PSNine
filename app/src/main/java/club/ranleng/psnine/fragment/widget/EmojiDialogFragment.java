package club.ranleng.psnine.fragment.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.EmojiDialogAdapter;
import club.ranleng.psnine.event.EmojiEvent;
import club.ranleng.psnine.widget.KEY;


public class EmojiDialogFragment extends DialogFragment {


    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        View view = getActivity().getLayoutInflater().inflate(R.layout.view_recycler, null);
        ButterKnife.bind(this,view);

        swipeRefreshLayout.setEnabled(false);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(8, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        final EmojiDialogAdapter emojiDialogAdapter = new EmojiDialogAdapter();
        emojiDialogAdapter.setClickListener(new EmojiDialogAdapter.onEmojiClick() {
            @Override
            public void onClick(String name) {
                EventBus.getDefault().post(new EmojiEvent(name));
                if(KEY.PREF_EMOJI){
                    EmojiDialogFragment.this.getDialog().dismiss();
                }
            }
        });
        recyclerView.setAdapter(emojiDialogAdapter);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EmojiDialogFragment.this.getDialog().dismiss();
            }
        });
        return builder.create();
    }
}
