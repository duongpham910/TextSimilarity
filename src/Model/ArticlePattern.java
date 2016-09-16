package Model;

/**
 * Created by DuongPham on 28/08/2016.
 */
public class ArticlePattern {
    private int id;
    private ItemNews sample;
    private ItemNews doc1;
    private ItemNews doc2;
    private int part;

    public ArticlePattern(int id, ItemNews sample, ItemNews doc1, ItemNews doc2, int part) {
        this.id = id;
        this.sample = sample;
        this.doc1 = doc1;
        this.doc2 = doc2;
        this.part = part;
    }

    public ArticlePattern() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ItemNews getSample() {
        return sample;
    }

    public void setSample(ItemNews sample) {
        this.sample = sample;
    }

    public ItemNews getDoc1() {
        return doc1;
    }

    public void setDoc1(ItemNews doc1) {
        this.doc1 = doc1;
    }

    public ItemNews getDoc2() {
        return doc2;
    }

    public void setDoc2(ItemNews doc2) {
        this.doc2 = doc2;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }
}
