package uz.pl.rss_application.model;

public class RssFeedModel {

    private String title;
    private String link;
    private String description;
    private String imageLink;

    public RssFeedModel(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public RssFeedModel() {
        this.title = "";
        this.link = "";
        this.description = "";
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLink() { return imageLink;  }

    public void setImageLink(String imageLink) {  this.imageLink = imageLink; }
}
