package uz.pl.rss_application;

import android.util.JsonReader;

import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import uz.pl.rss_application.model.RssChannelModel;

public class JsonChannelsReader {
    public List<RssChannelModel> getRssChannelModels(InputStream jsonFile) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(jsonFile, "UTF-8"));
        try {
            return readAllChannels(reader);
        } finally {
            reader.close();
        }
    }

    public List<RssChannelModel> readAllChannels(JsonReader reader) throws IOException {
        List<RssChannelModel> channels = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            RssChannelModel channel = readChannel(reader);
            if (channel != RssChannelModel.EMPTY) {
                channels.add(channel);
            }
        }
        reader.endArray();
        return channels;
    }

    public RssChannelModel readChannel(JsonReader reader) throws IOException {
        String channelName = "";
        String channelUrl = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                channelName = reader.nextString();
            } else if (name.equals("url")) {
                channelUrl = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (StringUtils.isEmpty(channelName) || StringUtils.isEmpty(channelUrl)){
            return RssChannelModel.EMPTY;
        }
        return new RssChannelModel(channelName, channelUrl);
    }
 }
