package uz.pl.rss_application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import uz.pl.rss_application.R;
import uz.pl.rss_application.model.RssChannelModel;
public class ChannelListAdapter extends ArrayAdapter<RssChannelModel> {
    public ChannelListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ChannelListAdapter(Context context, int resource, List<RssChannelModel> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.rss_channel, null);
        }
        RssChannelModel model = getItem(position);
        if (model != null) {
            TextView textView = (TextView) view.findViewById(R.id.rsschannelname);
            if (textView != null) {
                textView.setText(model.getName());
            }
        }
        return view;
    }
}
