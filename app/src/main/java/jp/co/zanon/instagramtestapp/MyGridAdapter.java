package jp.co.zanon.instagramtestapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shintaro1 on 15/07/23.
 */
public class MyGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = getClass().getSimpleName();
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<InstagramItem> mItems;

    public MyGridAdapter(Context context, List<InstagramItem> items){
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        mItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InstagramViewHolder(mLayoutInflater.inflate(R.layout.grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InstagramItem item = mItems.get(position);
        InstagramViewHolder instagramHolder = (InstagramViewHolder) holder;

        SquaredImageView imageView = (SquaredImageView) instagramHolder.getImageView();
        ProgressBar progressBar = instagramHolder.getProgressBar();

        progressBar.setVisibility(View.VISIBLE);
        // 画像を取得して挿入
        Picasso.with(this.context).load(item.thumbnail).into(imageView, new ImageLoadedCallback(null, progressBar));
    }

    @Override
    public int getItemCount() {
        if (mItems != null)
            return mItems.size();
        return 0;
    }
}
