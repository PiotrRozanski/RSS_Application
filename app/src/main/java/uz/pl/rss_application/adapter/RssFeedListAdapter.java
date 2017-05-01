package uz.pl.rss_application.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import uz.pl.rss_application.R;
import uz.pl.rss_application.model.RssFeedModel;

public class RssFeedListAdapter
        extends RecyclerView.Adapter<FeedModelViewHolder> {

    private List<RssFeedModel> mRssFeedModels;

    public RssFeedListAdapter(List<RssFeedModel> rssFeedModels) {
        mRssFeedModels = rssFeedModels;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rss_feed, parent, false);

        return new FeedModelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {
        final RssFeedModel rssFeedModel = mRssFeedModels.get(position);

        ((TextView) holder.getRssFeedView().findViewById(R.id.titleText)).setText(rssFeedModel.getTitle());
        ((TextView) holder.getRssFeedView().findViewById(R.id.descriptionText)).setText(rssFeedModel.getDescription());
        ((TextView) holder.getRssFeedView().findViewById(R.id.linkText)).setText(rssFeedModel.getLink());
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }
}

