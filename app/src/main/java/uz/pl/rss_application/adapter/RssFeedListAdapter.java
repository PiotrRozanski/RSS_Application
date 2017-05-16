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
    public FeedModelViewHolder onCreateViewHolder(final ViewGroup parent, int type) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rss_feed, parent, false);

        return new FeedModelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FeedModelViewHolder holder, int position) {
        final RssFeedModel rssFeedModel = mRssFeedModels.get(position);

        addRssMessageToView(holder, R.id.titleText, rssFeedModel.getTitle());
        addRssMessageToView(holder, R.id.descriptionText, rssFeedModel.getDescription());
        addRssMessageToView(holder, R.id.linkText, rssFeedModel.getLink());
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }

    private void addRssMessageToView(final FeedModelViewHolder holder,final int elementId, final String text) {
        ((TextView) holder.getRssFeedView().findViewById(elementId)).setText(text);
    }
}

