package Model;

/**
 * Created by DuongPham on 26/07/2016.
 */
public class ItemNews {
    private int id;
    private String title;
    private String description;
    private String link;
    private String date;
    private String content;
    private String publissher;

    public ItemNews() {
    }

    public ItemNews(int id, String title, String description, String link, String date, String content, String publissher) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = date;
        this.content = content;
        this.publissher = publissher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublissher() {
        return publissher;
    }

    public void setPublissher(String publissher) {
        this.publissher = publissher;
    }
}
