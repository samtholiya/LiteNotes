package com.trak.samtholiya.litenotes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.trak.samtholiya.litenotes.dummy.MainContent;

import java.util.Collections;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class LiteBooksFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private SharedPreferences pref;
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private LiteBookRecyclerViewAdapter liteBookAdapter;
    private RecyclerView.LayoutManager layoutManager;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LiteBooksFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LiteBooksFragment newInstance(int columnCount) {
        LiteBooksFragment fragment = new LiteBooksFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        Toast.makeText(fragment.getContext(),"fdf",Toast.LENGTH_LONG).show();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_litebook_list, container, false);

        // setupWindowAnimations();

        // Set the adapter
        pref= PreferenceManager.getDefaultSharedPreferences(view.getContext());

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                layoutManager=new LinearLayoutManager(context);

                recyclerView.setLayoutManager(layoutManager);
            } else {
                layoutManager=new GridLayoutManager(context, mColumnCount);
                recyclerView.setLayoutManager(layoutManager);
            }

            MainActivity.dummyContent=new MainContent(view.getContext());
            liteBookAdapter=new LiteBookRecyclerViewAdapter(MainActivity.dummyContent.getITEMS(), mListener,view.getContext());
            try {
                liteBookAdapter.setUndoOn(pref.getBoolean("example_switch", true));
            }catch(Exception r){
                Log.d("df","---------" + r.getMessage());
            }
            recyclerView.setAdapter(liteBookAdapter);
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                // we want to cache this and not allocate anything repeatedly in the onDraw method
                Drawable background;
                boolean initiated;

                private void init() {
                    background = new ColorDrawable(Color.YELLOW);
                    background.setAlpha(150);
                    initiated = true;
                }

                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                    if (!initiated) {
                        init();
                    }

                    // only if animation is in progress
                    if (parent.getItemAnimator().isRunning()) {

                        // some items might be animating down and some items might be animating up to close the gap left by the removed item
                        // this is not exclusive, both movement can be happening at the same time
                        // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                        // then remove one from the middle

                        // find first child with translationY > 0
                        // and last one with translationY < 0
                        // we're after a rect that is not covered in recycler-view views at this point in time
                        View lastViewComingDown = null;
                        View firstViewComingUp = null;

                        // this is fixed
                        int left = 0;
                        int right = parent.getWidth();

                        // this we need to find out
                        int top = 0;
                        int bottom = 0;

                        // find relevant translating views
                        int childCount = parent.getLayoutManager().getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            View child = parent.getLayoutManager().getChildAt(i);
                            if (child.getTranslationY() < 0) {
                                // view is coming down
                                lastViewComingDown = child;
                            } else if (child.getTranslationY() > 0) {
                                // view is coming up
                                if (firstViewComingUp == null) {
                                    firstViewComingUp = child;
                                }
                            }
                        }

                        if (lastViewComingDown != null && firstViewComingUp != null) {
                            // views are coming down AND going up to fill the void
                            top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                            bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                        } else if (lastViewComingDown != null) {
                            // views are going down to fill the void
                            top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                            bottom = lastViewComingDown.getBottom();
                        } else if (firstViewComingUp != null) {
                            // views are coming up to fill the void
                            top = firstViewComingUp.getTop();
                            bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                        }

                        background.setBounds(left, top, right, bottom);
                        background.draw(c);

                    }
                    super.onDraw(c, parent, state);
                }

            });

            MainActivity.dummyContent.setLiteBookAdapter(liteBookAdapter);
            MainActivity.dummyContent.setRecyclerView(recyclerView);
           // Toast.makeText(getContext(),String.valueOf(pref.getBoolean("example_switch",true)),Toast.LENGTH_LONG).show();
            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT ) {

                Drawable background;
                Drawable xMark;
                int xMarkMargin;
                boolean initiated;

                private void init() {
                    background = new ColorDrawable(Color.RED);
                    xMark = ContextCompat.getDrawable(getContext(), R.drawable.ic_delete_24dp);
                    xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    xMarkMargin = (int) LiteBooksFragment.this.getResources().getDimension(R.dimen.ic_clear_margin);
                    initiated = true;
                }

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    // get the viewHolder's and target's positions in your adapter data, swap them
                    Collections.swap(MainActivity.dummyContent.getITEMS(), viewHolder.getAdapterPosition(), target.getAdapterPosition());
                    // and notify the adapter that its dataset has changed
                    liteBookAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                    return true;
                }

                @Override
                public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    int position = viewHolder.getAdapterPosition();
                    LiteBookRecyclerViewAdapter testAdapter = (LiteBookRecyclerViewAdapter) recyclerView.getAdapter();
                    if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                        return 0;
                    }
                    return super.getSwipeDirs(recyclerView, viewHolder);
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    Log.d("here swiped",String.valueOf(swipeDir)+" h "+String.valueOf(viewHolder.getAdapterPosition()));

                    int position=viewHolder.getAdapterPosition();
                    LiteBookRecyclerViewAdapter adapter = (LiteBookRecyclerViewAdapter) recyclerView.getAdapter();
                    boolean undoOn = adapter.isUndoOn();
                    if (undoOn) {
                        adapter.pendingRemoval(position);
                    } else {
                        adapter.remove(position);
                    }
                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    View itemView = viewHolder.itemView;

                    // not sure why, but this method get's called for viewholder that are already swiped away
                    if (viewHolder.getAdapterPosition() == -1) {
                        // not interested in those
                        return;
                    }

                    if (!initiated) {
                        init();
                    }
                    // draw red background
                    Log.d("lets see",dX+" "+dY);
                    if(dX!=0) {
                        background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                        background.draw(c);

                        // draw x mark
                        int itemHeight = itemView.getBottom() - itemView.getTop();
                        int intrinsicWidth = xMark.getIntrinsicWidth();
                        int intrinsicHeight = xMark.getIntrinsicWidth();

                        int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                        int xMarkRight = itemView.getRight() - xMarkMargin;
                        int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                        int xMarkBottom = xMarkTop + intrinsicHeight;
                        xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);
                        xMark.draw(c);
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }

            };



            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
        return view;
    }

    /*private void removeView(){
                if(view.getParent()!=null) {
                        ((ViewGroup) view.getParent()).removeView(view);
                    }
            }
*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    Parcelable savedState;

    @Override
    public void onPause() {
        super.onPause();
        if(layoutManager!=null) {
            savedState = layoutManager.onSaveInstanceState();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        liteBookAdapter.setUndoOn(pref.getBoolean("example_switch", true));
        if(savedState!=null){
            layoutManager.onRestoreInstanceState(savedState);
        }
    }


}
