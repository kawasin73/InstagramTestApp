package jp.co.zanon.instagramtestapp;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by shintaro1 on 15/07/24.
 */
public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private final String TAG = getClass().getSimpleName();
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private int previousTotal = 0;
    private boolean loading = true;
    private int current_page = 1;

    private GridLayoutManager mGridLayoutManager;

    public EndlessScrollListener(GridLayoutManager gridLayoutManager) {
        this.mGridLayoutManager = gridLayoutManager;

        // カウントを初期化
        refresh();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mGridLayoutManager.getItemCount();
        firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();
        LogUtil.d(TAG, "visibleItemCount=" + visibleItemCount);
        LogUtil.d(TAG, "totalItemCount=" + totalItemCount);
        LogUtil.d(TAG, "firstVisibleItem=" + firstVisibleItem);
        LogUtil.d(TAG, "previousTotal=" + previousTotal);
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        if (!loading && (totalItemCount - 1) <= (firstVisibleItem + visibleItemCount)) {
            current_page++;

            onLoadMore(current_page);

            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);

    public void refresh() {
        this.previousTotal = 0;
        this.current_page = 1;
        this.loading = true;
    }
}
