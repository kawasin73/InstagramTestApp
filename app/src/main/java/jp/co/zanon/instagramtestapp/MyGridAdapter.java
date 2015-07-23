package jp.co.zanon.instagramtestapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shintaro1 on 15/07/23.
 */
public class MyGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = getClass().getSimpleName();
    private LayoutInflater mLayoutInflater;
    private ArrayList<InstagramItem> mItems;

    public MyGridAdapter(Context context, ArrayList<InstagramItem> items){
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
        instagramHolder.getTextView().setText(item.thumbnail);
        LogUtil.d(TAG, "onBindViewHolder: item.thumbnail="+item.thumbnail);
    }

    @Override
    public int getItemCount() {
        if (mItems != null)
            return mItems.size();
        return 0;
    }
}
