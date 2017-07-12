package club.ranleng.psnine.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.Common.EmojiDialogAdapter;
import club.ranleng.psnine.model.KEY;

/**
 * Created by ran on 08/07/2017.
 */

public class EmojiDialogFragment extends DialogFragment {


    @BindView(R.id.fragment_recyclerview) RecyclerView recyclerView;
    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;

    public interface EmojiDialogListener {
        void onSelected(String name);
    }

    EmojiDialogListener emojiDialogListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            emojiDialogListener = (EmojiDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        View view = getActivity().getLayoutInflater().inflate(R.layout.view_recyclerview, null);
        ButterKnife.bind(this,view);

        swipeRefreshLayout.setEnabled(false);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(8, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        final EmojiDialogAdapter emojiDialogAdapter = new EmojiDialogAdapter();
        emojiDialogAdapter.setClickListener(new EmojiDialogAdapter.onEmojiClick() {
            @Override
            public void onClick(String name) {
                emojiDialogListener.onSelected(name);
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
