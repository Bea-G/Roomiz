package com.example.roomiz;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class StackLayoutManager extends RecyclerView.LayoutManager {

    private final int visibleCount = 3;
    private final float translationYGap = 18f;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT
        );
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);

        if (getItemCount() == 0) {
            return;
        }

        int startPosition = Math.max(0, getItemCount() - visibleCount);

        for (int position = startPosition; position < getItemCount(); position++) {
            View view = recycler.getViewForPosition(position);
            addView(view);
            measureChildWithMargins(view, 0, 0);

            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);

            int left = (getWidth() - width) / 2;
            int top = (getHeight() - height) / 2;

            layoutDecorated(view, left, top, left + width, top + height);

            int level = getItemCount() - 1 - position;

            view.setTranslationX(0f);
            view.setTranslationY(-level * translationYGap);

            view.setScaleX(1f);
            view.setScaleY(1f);
            view.setAlpha(1f);
        }
    }
}