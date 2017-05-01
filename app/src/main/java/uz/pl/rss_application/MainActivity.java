package uz.pl.rss_application;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import uz.pl.rss_application.adapter.RssFeedListAdapter;
import uz.pl.rss_application.model.RssFeedModel;
import uz.pl.rss_application.parser.XmlParser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private EditText editText;
    private SwipeRefreshLayout swipeLayout;
    private TextView feedTitleTextView;
    private TextView feedLinkTextView;
    private TextView feedDescriptionTextView;

    private List<RssFeedModel> feedModelList;

    private XmlParser xmlParser = new XmlParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button fetchFeedButton = (Button) findViewById(R.id.fetchFeedButton);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        editText = (EditText) findViewById(R.id.rssFeedEditText);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        feedTitleTextView = (TextView) findViewById(R.id.feedTitle);
        feedDescriptionTextView = (TextView) findViewById(R.id.feedDescription);
        feedLinkTextView = (TextView) findViewById(R.id.feedLink);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchFeedTask().execute((Void) null);
            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchFeedTask().execute((Void) null);
            }
        });
    }

    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        private String urlLink;

        @Override
        protected void onPreExecute() {
            swipeLayout.setRefreshing(true);
            feedTitleTextView.setText("Feed Title: ");
            feedDescriptionTextView.setText("Feed Description: ");
            feedLinkTextView.setText("Feed Link: ");
            urlLink = editText.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if (!urlLink.startsWith("http://") && !urlLink.startsWith("https://")) {
                    urlLink = "http://" + urlLink;
                }

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                feedModelList = xmlParser.parseXmlFeed(inputStream);
                return true;

            } catch (IOException | XmlPullParserException e) {
                Log.e(TAG, "ERROR", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            swipeLayout.setRefreshing(false);

            if (success) {
                RssFeedModel rssHeading = xmlParser.getRssHeading();
                feedTitleTextView.setText("Feed Title: " + rssHeading.getTitle());
                feedDescriptionTextView.setText("Feed Description: " + rssHeading.getDescription());
                feedLinkTextView.setText("Feed Link: " + rssHeading.getLink());
                // Fill RecyclerView
                recyclerView.setAdapter(new RssFeedListAdapter(feedModelList));
            } else {
                Toast.makeText(MainActivity.this,
                        "Enter a valid Rss feed url",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
