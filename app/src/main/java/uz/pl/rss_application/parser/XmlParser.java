package uz.pl.rss_application.parser;

import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import uz.pl.rss_application.model.RssFeedModel;

public class XmlParser {

    private final Pattern pattern;
    private String title;
    private String link;
    private String description;
    private String image;
    private XmlPullParser xmlPullParser;

    public XmlParser() {
        this.xmlPullParser = Xml.newPullParser();
        pattern = Pattern.compile("-\\d{1,4}x\\d{1,4}");
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
                        item.setImageLink(image);
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
            if (result != null) {
                image = pullImageLink(result);
                description = Utils.fromHtml(result.replaceAll("<img.+?>", "")).toString();
            }
        }
    }
    private String pullImageLink(String encoded) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(encoded));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && "img".equals(xpp.getName())) {
                    int count = xpp.getAttributeCount();
                    for (int x = 0; x < count; x++) {
                        if (xpp.getAttributeName(x).equalsIgnoreCase("src"))
                            return pattern.matcher(xpp.getAttributeValue(x)).replaceAll("");
                    }
                }
                eventType = xpp.next();
            }
        }
        catch (XmlPullParserException|IOException e){
            Log.e("XmlParsing", e.getMessage());
        }

        return "";
    }
}
