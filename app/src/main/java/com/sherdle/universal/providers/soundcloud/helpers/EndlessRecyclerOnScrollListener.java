package com.sherdle.universal.providers.soundcloud.helpers;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

//https://gist.github.com/ssinss/e06f12ef66c51252563e
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private boolean forceCantLoadMore = false; //Force the stop of loading more

    private int current_page = 0;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold) && !forceCantLoadMore) {
            // End has been reached

            // Do something
            current_page++;

            onLoadMore(current_page);

            loading = true;
        }
    }

    public void reset() {
        current_page = 0;
        loading = false;
        forceCantLoadMore = false;
        previousTotal = 0;
    }

    public abstract void onLoadMore(int current_page);

    public void forceCantLoadMore(boolean forceCantLoadMore){
        this.forceCantLoadMore = forceCantLoadMore;
    }
}
