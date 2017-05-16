package uz.pl.rss_application.parser;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import uz.pl.rss_application.model.RssFeedModel;

public class XmlParser {

    private String title;
    private String link;
    private String description;
    private XmlPullParser xmlPullParser;

    public XmlParser() {
        this.xmlPullParser = Xml.newPullParser();
    }

    public List<RssFeedModel> parseXmlFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        boolean isItem = false;
        List<RssFeedModel> items = new ArrayList<>();

        try (final InputStream input = inputStream) {
            String name;

            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(input, null);

            xmlPullParser.nextTag();

            if (xmlPullParser.getName().equalsIgnoreCase("channel")) {
                xmlPullParser.nextTag();

                if (xmlPullParser.getName().equalsIgnoreCase("title")) {
                    name = xmlPullParser.getName();
                    checkRssHeading(name);
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

                if (title != null && link != null && description != null) {
                    if (isItem) {
                        RssFeedModel item = new RssFeedModel(title, link, description);
                        items.add(item);
                    }
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
            title = result;
        } else if (name.equalsIgnoreCase("link")) {
            link = result;
        } else if (name.equalsIgnoreCase("description")) {
            description = result;
        }
    }
}
