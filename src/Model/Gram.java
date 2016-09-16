package Model;

/**
 * Created by DuongPham on 10/04/2016.
 */
public class Gram {
    private String word;
    private int counter;
    private double tfidf;

    public Gram() {
    }

    public Gram(String word, int counter, double tfidf) {
        this.word = word;
        this.counter = counter;
        this.tfidf = tfidf;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public double getTfidf() {
        return tfidf;
    }

    public void setTfidf(double tfidf) {
        this.tfidf = tfidf;
    }
}
