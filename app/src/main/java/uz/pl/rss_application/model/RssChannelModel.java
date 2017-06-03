package uz.pl.rss_application.model;

public class RssChannelModel {
    public final static RssChannelModel EMPTY = new RssChannelModel("","");
    final private String name;
    final private String link;

    public RssChannelModel(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }
}
