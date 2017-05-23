package uz.pl.rss_application.model;

/**
 * Created by Robert on 23.05.2017.
 */

public class RssChannelModel {
    private String name;
    private String link;

    public RssChannelModel(String name, String link){
        this.name=name;
        this.link=link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }
}
