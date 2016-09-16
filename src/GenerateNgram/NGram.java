package GenerateNgram;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by DuongPham on 04/04/2016.
 */
public class NGram {

    public static void generateNGram(int N, String text) {
        //digits and punctuantion are discared
        text = text.replaceAll("\\`|\\~|\\!|\\@|\\#|\\$|\\%|\\^|\\&|\\*|\\(|"
                + "\\)|\\_|\\=|\\+|\\{|\\[|\\}|\\]|\\<|\\,|\\>|\\.|\\?|"
                + "\\/|\\;|\\:|\\d+", "");

        String[] tokens = text.split("\\s+");
        ArrayList<String> listGram = new ArrayList<String>();
        ArrayList<Integer> counters = new ArrayList<Integer>();

        for (int i = 0; i < tokens.length - N + 1; i++) {
            String s = "";
            int start = i;
            int end = i + N;
            for (int j = start; j < end; j++) {
                s = s + " " + tokens[j];
            }

            boolean flag = true;
            int counter = 0;
            if (listGram.size() != 0) {
                for (int k = 0; k < listGram.size(); k++) {
                    String temp = listGram.get(k);
                    if (temp.equals(s)) {
                        counter = counters.get(k);
                        counter++;
                        counters.set(k, counter);
                        flag = false;
                    }
                }
            }
            if (flag == true) {
                listGram.add(s);
                counters.add(1);
            }
        }


        for (int i = 0; i < listGram.size(); i++) {
            String temp = listGram.get(i);
            System.out.println(temp + " " + counters.get(i));
        }
    }

    public static void main(String[] args) {
        String text = "ant ant bee tempa tempb tempc";
        generateNGram(1, text);

    }
}
