package uz.pl.rss_application.parser;

import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Pattern;

/**
 * Created by Robert on 21.05.2017.
 */

public class Utils {
    private static final Pattern IMG_PATTERN;

    static{
        IMG_PATTERN = Pattern.compile("-\\d{1,4}x\\d{1,4}");
    }
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

    public static String stripImgTag(String result) {
        return Utils.fromHtml(result.replaceAll("<img.+?>", "")).toString();
    }

    public static String pullImageLink(String encoded) {
        String result="";
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
                            result = IMG_PATTERN.matcher(xpp.getAttributeValue(x)).replaceAll("");
                    }
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e("XmlParsing", e.getMessage());
        }

        return result;
    }
}
