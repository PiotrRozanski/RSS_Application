package uz.pl.rss_application.parser;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
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

    public List<RssFeedModel> parseXmlFeed(final InputStream inputStream) throws XmlPullParserException, IOException {
        final List<RssFeedModel> items = new ArrayList<>();

        try (final InputStream input = inputStream) {
            String name;

            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(input, null);

            xmlPullParser.nextTag();
            xmlPullParser.nextTag();

            ignoreHeading();

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

    private void ignoreHeading() throws XmlPullParserException, IOException {
        if (xmlPullParser.getName().equalsIgnoreCase("channel")) {
            while (!xmlPullParser.getName().equalsIgnoreCase("item")) {
                xmlPullParser.next();
                while (xmlPullParser.getName() == null) {
                    xmlPullParser.next();
                }
                if (xmlPullParser.getName().equalsIgnoreCase("item")) break;
            }
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
