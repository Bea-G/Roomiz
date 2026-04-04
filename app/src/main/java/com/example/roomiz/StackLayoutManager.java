package com.example.roomiz;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class StackLayoutManager extends RecyclerView.LayoutManager {
    // Layout manager for the profile cards

    private final int visibleCount = 3;  // Max number of visible cards
    private final float translationYGap = 18f;  // Gap between cards

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT
        );
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);

        if (getItemCount() == 0) {
            return;
        }

        // Make sure only visibleCount cards are visible
        int startPosition = Math.max(0, getItemCount() - visibleCount);

        // Loop through the items that should be visible
        for (int position = startPosition; position < getItemCount(); position++) {
            View view = recycler.getViewForPosition(position);  // Get a view for this position
            addView(view);  // Add it to the layout
            measureChildWithMargins(view, 0, 0);

            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);

            // Calculate center position inside RecyclerView
            int left = (getWidth() - width) / 2;
            int top = (getHeight() - height) / 2;

            // Place the view on the center
            layoutDecorated(view, left, top, left + width, top + height);

            int level = getItemCount() - 1 - position;  // Calculate how deep this card is in the stack

            view.setTranslationX(0f);  // Keep all cards horizontally centered
            view.setTranslationY(-level * translationYGap);  // Move cards slightly upward based on their depth

            // Keep all cards the same size
            view.setScaleX(1f);
            view.setScaleY(1f);
            view.setAlpha(1f);  // Fully visible
        }
    }
}