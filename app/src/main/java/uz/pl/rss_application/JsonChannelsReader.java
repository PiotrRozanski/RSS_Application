package uz.pl.rss_application;

import android.util.JsonReader;

import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import uz.pl.rss_application.model.RssChannelModel;

class JsonChannelsReader {
    List<RssChannelModel> getRssChannelModels(final InputStream jsonFile) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(jsonFile, "UTF-8"))) {
            return readAllChannels(reader);
        }
    }

    private List<RssChannelModel> readAllChannels(final JsonReader reader) throws IOException {
        final List<RssChannelModel> channels = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            final RssChannelModel channel = readChannel(reader);
            if (channel != RssChannelModel.EMPTY) {
                channels.add(channel);
            }
        }
        reader.endArray();
        return channels;
    }

    private RssChannelModel readChannel(final JsonReader reader) throws IOException {
        String channelName = "";
        String channelUrl = "";

        reader.beginObject();
        while (reader.hasNext()) {
            final String name = reader.nextName();
            switch (name) {
                case "name":
                    channelName = reader.nextString();
                    break;
                case "url":
                    channelUrl = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        if (StringUtils.isEmpty(channelName) || StringUtils.isEmpty(channelUrl)){
            return RssChannelModel.EMPTY;
        }
        return new RssChannelModel(channelName, channelUrl);
    }
 }
