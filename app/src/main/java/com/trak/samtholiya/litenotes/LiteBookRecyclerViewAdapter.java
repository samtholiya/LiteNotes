package com.trak.samtholiya.litenotes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

//import com.trak.samtholiya.litenotes.ItemFragment.OnListFragmentInteractionListener;
import com.trak.samtholiya.litenotes.dummy.MainContent;
import com.trak.samtholiya.litenotes.dummy.MainContent.MainItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MainContent.MainItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class LiteBookRecyclerViewAdapter extends RecyclerView.Adapter<LiteBookRecyclerViewAdapter.ViewHolder> {

    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec

    private final List<MainItem> mValues;
    private final List<MainItem> mItemsPendingRemoval;
    private final OnListFragmentInteractionListener mListener;
    private final Context mContext;

    int lastInsertedIndex; // so we can add some more items for testing purposes
    boolean undoOn=true; // is undo on, you can turn it on from the toolbar menu
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<MainItem, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be



    private SharedPreferences pref;


    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position) {
        final MainContent.MainItem item = mValues.get(position);
        if (!mItemsPendingRemoval.contains(item)) {
            mItemsPendingRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(mValues.indexOf(item));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        MainContent.MainItem item = mValues.get(position);
        if (mItemsPendingRemoval.contains(item)) {
            mItemsPendingRemoval.remove(item);
        }
        if (mValues.contains(item)) {
            MainActivity.dummyContent.deleteData(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, MainActivity.dummyContent.getITEMS().size());
        }
    }

    public boolean isPendingRemoval(int position) {
        MainItem item = mValues.get(position);
        return mItemsPendingRemoval.contains(item);
    }

    public LiteBookRecyclerViewAdapter(List<MainContent.MainItem> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        mContext=context;
        mItemsPendingRemoval =new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_litebook, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MainItem item=mValues.get(position);

        if (mItemsPendingRemoval.contains(item)) {
            // we need to show the "undo" state of the row
            holder.itemView.setBackgroundColor(Color.RED);
            holder.mIdView.setVisibility(View.GONE);
            holder.mContentView.setVisibility(View.GONE);
            holder.undoButton.setVisibility(View.VISIBLE);

            holder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(item);
                    pendingRunnables.remove(item);
                    if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                    mItemsPendingRemoval.remove(item);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(mValues.indexOf(item));
                }
            });
        }
        else {

            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            holder.mIdView.setVisibility(View.VISIBLE);
            holder.mContentView.setVisibility(View.VISIBLE);
            holder.undoButton.setVisibility(View.GONE);

            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).title);
            holder.mContentView.setText(mValues.get(position).details);
            pref = PreferenceManager.getDefaultSharedPreferences(mContext);
            if (Build.VERSION.SDK_INT >= 23) {
                holder.mIdView.setTextAppearance(Integer.parseInt(pref.getString(mContext.getResources().getString(R.string.liteTitleFont), "7f09016e").substring(2), 16));
                holder.mContentView.setTextAppearance(Integer.parseInt(pref.getString(mContext.getResources().getString(R.string.liteDetailFont), "7f09016e").substring(2), 16));

            } else {
                holder.mIdView.setTextAppearance(mContext, Integer.parseInt(pref.getString(mContext.getResources().getString(R.string.liteTitleFont), "7f09016e").substring(2), 16));
                holder.mContentView.setTextAppearance(mContext, Integer.parseInt(pref.getString(mContext.getResources().getString(R.string.liteDetailFont), "7f09016e").substring(2), 16));
            }
            holder.mIdView.setTypeface(holder.mIdView.getTypeface(), Typeface.BOLD);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(holder.mItem);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public MainContent.MainItem mItem;
        public final Button undoButton;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.title);
            undoButton = (Button) view.findViewById(R.id.undo_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
