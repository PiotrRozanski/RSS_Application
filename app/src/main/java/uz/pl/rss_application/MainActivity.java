package uz.pl.rss_application;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uz.pl.rss_application.adapter.ChannelListAdapter;
import uz.pl.rss_application.adapter.RssFeedListAdapter;
import uz.pl.rss_application.model.RssChannelModel;
import uz.pl.rss_application.model.RssFeedModel;
import uz.pl.rss_application.parser.XmlParser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; 
    private static final String DEFAULT_RSS = "http://www.rmf24.pl/fakty/feed";
    private String currentRssLink;
 

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;

    private DrawerLayout drawerLayout;
    private ListView channelsListView;

    private List<RssFeedModel> feedModelList;

    private XmlParser xmlParser = new XmlParser();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViewElementAction();
        fillRssChannels();
        setTitle("Wybierz kanał RSS z lewej");
    }

    ArrayList<RssChannelModel> channels;

    private void fillRssChannels() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        channelsListView = (ListView) findViewById(R.id.channel_list);
        channels = new ArrayList<>(Arrays.asList(
                new RssChannelModel("RMF 24 - Fakty z Polski", "http://www.rmf24.pl/fakty/polska/feed"),
                new RssChannelModel("RMF 24 - Fakty ze świata",  "http://www.rmf24.pl/fakty/swiat/feed"),
                new RssChannelModel("Wirtualna Polska - film", "http://film.wp.pl/rss.xml"),
                new RssChannelModel("Wirtualna Polska - moto", "http://moto.wp.pl/rss.xml")));

        ChannelListAdapter adapter = new ChannelListAdapter(this, R.layout.rss_channel, channels);
        channelsListView.setAdapter(adapter);
        channelsListView.setOnItemClickListener(new DrawerItemClickListener());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        RssChannelModel model = channels.get(position);
        currentRssLink = model.getLink();
        fetchRss();

        channelsListView.setItemChecked(position, true);
        setTitle(model.getName());
        drawerLayout.closeDrawer(findViewById(R.id.left_drawer));
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }


    private void setViewElementAction() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchRss();
            }
        });
    }

    private void fetchRss() {
        new FetchFeedTask().execute((Void) null);
    }

    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            swipeLayout.setRefreshing(true);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(currentRssLink))
                return false;

            try {
                if (!currentRssLink.startsWith("http://") && !currentRssLink.startsWith("https://")) {
                    currentRssLink = "http://" + currentRssLink;
                }

                final URL url = new URL(currentRssLink);
                final InputStream inputStream = url.openConnection().getInputStream();
                feedModelList = xmlParser.parseXmlFeed(inputStream);
                return true;

            } catch (final IOException | XmlPullParserException e) {
                Log.e(TAG, "ERROR", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            swipeLayout.setRefreshing(false);

            if (success) {
                recyclerView.setAdapter(new RssFeedListAdapter(feedModelList));
            } else {
                Toast.makeText(MainActivity.this,
                        "Invalid link to RSS",
                        Toast.LENGTH_LONG).show();
            }
        }

    }
}
