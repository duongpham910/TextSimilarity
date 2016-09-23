package GenerateNgram;

import Model.Gram;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Created by DuongPham on 21/04/2016.
 */
public class Out_Of_Place {

    public String Filter(String temp){
        String text = temp.replaceAll("\\`|\\~|\\!|\\@|\\#|\\$|\\%|\\^|\\&|\\*|\\(|"
                + "\\)|\\_|\\=|\\+|\\{|\\[|\\}|\\]|\\<|\\,|\\>|\\.|\\?|"
                + "\\/|\\;|\\:|\\-|\\d+", "");
        text =text.toLowerCase();
        return text;
    }

    public int outofplace(ArrayList<Gram> doc1,ArrayList<Gram> doc2){
        int DistanceMeasure=0;
        int max=0;
        int count=0;
        for(int i=0;i<200;i++){
            Gram g=doc2.get(i);
            boolean flag=true;
            for(int j=0;j<200;j++){
                Gram temp=doc1.get(j);
                if(g.getWord().equals(temp.getWord())){
                    int pos=Math.abs(j-i);
                    flag=false;
                    if(pos>max){
                        max=pos;
                    }
                    DistanceMeasure=DistanceMeasure+pos;
                    break;
                }
            }
            if(flag==true){
                count++;
            }
        }
        DistanceMeasure=DistanceMeasure+count*max;
        return DistanceMeasure;
    }


    public ArrayList<Gram> readFile(String fileName){
        ArrayList<Gram> listGram = new ArrayList<Gram>();
        try {
            int N=1; // bi-gram
            Scanner input=new Scanner(new FileInputStream(fileName));
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
            Collections.sort( listGram, new GramComparator());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return listGram;
    }

    public float balanceTFIDF(int result){
        String temp=String.valueOf(result);
        temp=temp.substring(0,2);
        float bal=Float.parseFloat(temp);
        bal=bal/10;
        return bal;
    }

    public static void main(String[] args) {
        Out_Of_Place oof=new Out_Of_Place();
        //ArrayList<Gram> doc1=ti.readFile("Cameron.txt");
        //ArrayList<Gram> doc2=ti.readFile("Cameron1.txt");
        ArrayList<Gram> doc3=oof.readFile("textExample/Brexit-batdongsan.txt");
        ArrayList<Gram> doc4=oof.readFile("Cameron1.txt");
        int result=oof.outofplace(doc3,doc4);
        System.out.println("Out of place = " + result);
        System.out.println("Out of place after balance:"+ oof.balanceTFIDF(result));
        //System.out.println("TF-IDF (the British) = " + tfidf);
    }

}
