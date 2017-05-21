package uz.pl.rss_application.parser;

import android.net.Uri;
import android.util.Log;
import android.util.Xml;

import org.apache.commons.lang3.StringUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import uz.pl.rss_application.model.RssFeedModel;

public class XmlParser {

    private RssFeedModel rssFeed;
    private boolean isItem = false;
    private XmlPullParser xmlPullParser;

    public XmlParser() {
        this.xmlPullParser = Xml.newPullParser();
        rssFeed = new RssFeedModel();
    }

    public List<RssFeedModel> parseXmlFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        List<RssFeedModel> items = new ArrayList<>();

        try (final InputStream input = inputStream) {
            String name;

            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(input, null);

            xmlPullParser.nextTag();
            xmlPullParser.nextTag();

            if (xmlPullParser.getName().equalsIgnoreCase("channel")) {
                while(!xmlPullParser.getName().equalsIgnoreCase("item")){
                    xmlPullParser.next();
                    xmlPullParser.next();
                }
            }

            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                final int eventType = xmlPullParser.getEventType();

                name = xmlPullParser.getName();

                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MainActivity", "Parsing name ==> " + name);

                checkRssHeading(name);

                if (rssFeed.isAllSet()) {
                    if (isItem) {
                        items.add(rssFeed);
                    }
                    rssFeed = new RssFeedModel();
                    isItem = false;
                }
            }
            return items;
        }
    }

    private void checkRssHeading(final String name) throws IOException, XmlPullParserException {
        String result = "";

        if (xmlPullParser.next() == XmlPullParser.TEXT) {
            result = xmlPullParser.getText();
            xmlPullParser.nextTag();
        }
            if (name.equalsIgnoreCase("title")) {
                rssFeed.setTitle(result);
            } else if (name.equalsIgnoreCase("link")) {
                rssFeed.setLink(result);
            } else if (name.equalsIgnoreCase("description")) {
                rssFeed.setImageLink(Utils.pullImageLink(result));
                rssFeed.setDescription(Utils.stripImgTag(result));
                isItem = true;
            }
    }
}
