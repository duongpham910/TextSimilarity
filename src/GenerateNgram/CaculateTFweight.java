package GenerateNgram;

import Model.Gram;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by DuongPham on 10/04/2016.
 */
public class CaculateTFweight {

    ArrayList<Gram> listGram = new ArrayList<Gram>();

    public String Filter(String tam){
        String text = tam.replaceAll("\\`|\\~|\\!|\\@|\\#|\\$|\\%|\\^|\\&|\\*|\\(|"
                + "\\)|\\_|\\=|\\+|\\{|\\[|\\}|\\]|\\<|\\,|\\>|\\.|\\?|"
                + "\\/|\\;|\\:|\\-|\\d+", "");
        text =text.toLowerCase();
        return text;
    }

    public void readFile(){
        try {
            int N=1; // bi-gram
            Scanner input=new Scanner(new FileInputStream("textExample/test1.txt"));
            while(input.hasNextLine()){
                String text=input.nextLine();
                text =Filter(text);

                String[] tokens = text.split("\\s+");
                for (int i = 0; i < tokens.length - N + 1; i++) {
                    String s = "";
                    int start = i;
                    int end = i + N;
                    for (int j = start; j < end; j++) {
                        s = s + " " + tokens[j];
                    }
                    s=s.replaceAll("^\\s+", "");
                    boolean flag = true;
                    int counter = 0;
                    if (listGram.size() != 0) {
                        for (int k = 0; k < listGram.size(); k++) {
                            String temp = listGram.get(k).getWord();
                            if (temp.equals(s)) {
                                counter = listGram.get(k).getCounter();
                                counter++;
                                listGram.get(k).setCounter(counter);
                                flag = false;
                            }
                        }
                    }
                    if (flag == true) {
                        Gram g=new Gram(s,1,0);
                        listGram.add(g);
                    }
                }
            }

            Collections.sort(listGram, new GramComparator());

            for (int i = 0; i < listGram.size(); i++) {
                String temp = listGram.get(i).getWord();
                int counter=listGram.get(i).getCounter();
                System.out.println(temp + " " + counter);
            }
            System.out.println(listGram.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CaculateTFweight c=new CaculateTFweight();
        c.readFile();
    }
}
