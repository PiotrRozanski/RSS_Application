package uz.pl.rss_application.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uz.pl.rss_application.R;
import uz.pl.rss_application.model.RssFeedModel;

public class RssFeedListAdapter extends RecyclerView.Adapter<FeedModelViewHolder> {
    public enum RSS_VIEW_TYPE {RSS_VIEW_1, RSS_VIEW_2, RSS_VIEW_3};
    private List<RssFeedModel> mRssFeedModels;
    private RSS_VIEW_TYPE type;

    public RssFeedListAdapter(List<RssFeedModel> rssFeedModels, RSS_VIEW_TYPE type) {
        mRssFeedModels = rssFeedModels;
        this.type = type;
    }
    
    @Override
    public FeedModelViewHolder onCreateViewHolder(final ViewGroup parent, int type) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rss_feed, parent, false);

        return new FeedModelViewHolder(v, mRssFeedModels, parent.getContext());

    }

    @Override
    public void onBindViewHolder(final FeedModelViewHolder holder, int position) {
        final RssFeedModel rssFeedModel = mRssFeedModels.get(position);

        if(mRssFeedModels != null) {
            addRssMessageToView(holder, R.id.titleText, rssFeedModel.getTitle());
            if (type.equals(RSS_VIEW_TYPE.RSS_VIEW_2)) {
                addRssMessageToView(holder, R.id.descriptionText, prepareDescription(rssFeedModel.getDescription()));
            } else if (type.equals(RSS_VIEW_TYPE.RSS_VIEW_3)) {
                addRssMessageToView(holder, R.id.descriptionText, prepareDescription(rssFeedModel.getDescription()));
                drawImage(holder, rssFeedModel);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }

    private void addRssMessageToView(final FeedModelViewHolder holder,final int elementId, final String text) {
        ((TextView) holder.getRssFeedView().findViewById(elementId)).setText(text);
    }

    private String prepareDescription(final String description) {
        return description.replaceAll("\n", "");
    }

    private void drawImage(final FeedModelViewHolder holder, final RssFeedModel rssFeedModel) {
        ImageView imageView=(ImageView) holder.getRssFeedView().findViewById(R.id.image);
        imageView.setImageBitmap(rssFeedModel.getImage());
    }

}

