package com.xjgj.mall.inter;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by WE-WIN-027 on 2016/9/19.
 *
 * @des ${TODO}
 */
public abstract class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener{

    private final RecyclerView recyclerView;
    private final GestureDetectorCompat mGestureDetector;

    public OnRecyclerItemClickListener(RecyclerView recyclerView){
        this.recyclerView=recyclerView;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(),new ItemTouchHelperGestureListener());
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    public abstract void onItemClick(RecyclerView.ViewHolder viewHolder);

    public abstract void onItemLongClick(RecyclerView.ViewHolder viewHolder);

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {

        public  boolean onSingleTapUp(MotionEvent event){
            View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
            if (child != null){
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
                onItemClick(viewHolder);
            }
            return true;
        }

        public  void onLongPress(MotionEvent event){
            View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
            if (child != null){
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
                onItemLongClick(viewHolder);
            }
        }
    }

}
