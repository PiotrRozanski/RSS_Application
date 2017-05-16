package uz.pl.rss_application.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

class FeedModelViewHolder extends RecyclerView.ViewHolder {

    private View rssFeedView;

    FeedModelViewHolder(View view) {
        super(view);
        rssFeedView = view;
    }

    View getRssFeedView() {
        return rssFeedView;
    }
}