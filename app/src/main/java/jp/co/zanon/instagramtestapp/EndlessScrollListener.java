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

        visibleItemCount = recyclerView.getChildCount();                      // 見えているアイテムの数
        totalItemCount = mGridLayoutManager.getItemCount();                   // アダプターにセットされた全てのアイテムの数
        firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition(); // 見えているアイテムの上にある見えていないアイテムの数
        LogUtil.d(TAG, "visibleItemCount=" + visibleItemCount);
        LogUtil.d(TAG, "totalItemCount=" + totalItemCount);
        LogUtil.d(TAG, "firstVisibleItem=" + firstVisibleItem);
        LogUtil.d(TAG, "previousTotal=" + previousTotal);

        // totalItemCountが増えたことを検出した時
        if (loading) {
            if (totalItemCount > previousTotal) {
                // ロックを解除
                loading = false;
                // totalItemCountの値を保存
                previousTotal = totalItemCount;
            }
        }
        // 一番下までスクロールしたことを検出した時
        if (!loading && (totalItemCount - 1) <= (firstVisibleItem + visibleItemCount)) {
            current_page++;

            onLoadMore(current_page);
            // totalItemCountの値が増えるまで、ロックをかける
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
