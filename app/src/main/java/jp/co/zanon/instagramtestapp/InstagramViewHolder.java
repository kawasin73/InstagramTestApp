package jp.co.zanon.instagramtestapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shintaro1 on 15/07/23.
 */
public class InstagramViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.textView)
    TextView mTextView;

    public InstagramViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getTextView() {
        return mTextView;
    }
}
