package jp.co.zanon.instagramtestapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shintaro1 on 15/07/23.
 */
public class InstagramViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.image_view)
    SquaredImageView mImageView;
    @Bind(R.id.prog_bar)
    ProgressBar mProgressBar;

    public InstagramViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }
}
