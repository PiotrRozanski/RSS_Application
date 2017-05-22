package uz.pl.rss_application.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import uz.pl.rss_application.model.RssFeedModel;

class FeedModelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private View rssFeedView;
    private  List<RssFeedModel> list;
    private Context c;

    FeedModelViewHolder(View view, List<RssFeedModel> mRssFeedModels, Context c) {
        super(view);
        this.list = mRssFeedModels;
        rssFeedView = view;
        view.setOnClickListener(this);
        this.c = c;
    }

    View getRssFeedView() {
        return rssFeedView;
    }

    @Override
    public void onClick(View view) {
        int index = getAdapterPosition();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.list.get(index).getLink()));
        c.startActivity(browserIntent);
    }
}