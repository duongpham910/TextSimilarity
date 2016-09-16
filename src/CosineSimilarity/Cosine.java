package CosineSimilarity;

import Model.Gram;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by DuongPham on 17/07/2016.
 */
public class Cosine {
    public String Filter(String tam){
        String text = tam.replaceAll("\\`|\\~|\\!|\\@|\\#|\\$|\\%|\\^|\\&|\\*|\\(|"
                + "\\)|\\_|\\=|\\+|\\{|\\[|\\}|\\]|\\<|\\,|\\>|\\.|\\?|"
                + "\\/|\\;|\\:|\\-|\\d+", "");
        text =text.toLowerCase();
        return text;
    }

    public double tf(ArrayList<Gram> doc, String term) {
        double result = 0;
        int sum=0;
        for(int i=0;i<doc.size();i++){
            if(doc.get(i).getWord().equals(term)){
                result=doc.get(i).getCounter();
            }
            sum=sum+doc.get(i).getCounter();
        }
        return result / sum;
    }

    public double idf(ArrayList<ArrayList<Gram>> docs, String term) {
        double n = 0;
        for (int i=0;i<docs.size();i++) {
            for (int j=0;j<docs.get(i).size();j++) {
                Gram g=docs.get(i).get(j);
                if (term.equals(g.getWord())) {
                    n++;
                    break;
                }
            }
        }

       if(n>0){
           return Math.log(docs.size()/(n))+1;
       }else{
           return 1;
       }
    }

    public double tfIdf(ArrayList<Gram> doc, ArrayList<ArrayList<Gram>> docs, String term) {
        //System.out.println(term+" "+tf(doc, term)+" "+idf(docs, term));
        return tf(doc, term) * idf(docs, term);

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
            //System.out.println(listGram.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return listGram;
    }

    public ArrayList<Gram> readText(String text) {
        ArrayList<Gram> listGram = new ArrayList<Gram>();
        int N = 1; // bi-gram
        text = Filter(text);
        String[] tokens = text.split("\\s+");
        for (int i = 0; i < tokens.length - N + 1; i++) {
            String s = "";
            int start = i;
            int end = i + N;
            for (int j = start; j < end; j++) {
                s = s + " " + tokens[j];
            }
            s = s.replaceAll("^\\s+", "");
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
                Gram g = new Gram(s, 1, 0);
                listGram.add(g);
            }
        }
        return listGram;
    }

    public ArrayList<String> BagOfWords(ArrayList<ArrayList<Gram>> docs){
        ArrayList<String> bog=new ArrayList<String>();
        for (int i=0;i<docs.size();i++) {
            for (int j=0;j<docs.get(i).size();j++) {
                Gram g=docs.get(i).get(j);
                if(bog.contains(g.getWord())){

                }else{
                    bog.add(g.getWord());
                }
            }
        }
        return bog;
    }

    public ArrayList<Gram> getVector(ArrayList<String> bog,ArrayList<Gram> doc){
        ArrayList<Gram> arrayList=new ArrayList<Gram>();
        for(int i=0;i<bog.size();i++){
            String word=bog.get(i);
            boolean flag=false;
            double temp=0;
            for(int j=0;j<doc.size();j++){
                Gram g=doc.get(j);
                if(g.getWord().equals(word)){
                    flag=true;
                    temp=g.getTfidf();
                }
            }
            if(flag==true){
                arrayList.add(new Gram(word,0,temp));
            }else{
                arrayList.add(new Gram(word,0,0));
            }
        }
        return arrayList;
    }

    public double determineCosine(ArrayList<Gram> v1,ArrayList<Gram> v2){
        double lengthOfV1=0.0;
        double lengthOfV2=0.0;
        double dotproduct=0.0;
        for(int i=0;i<v1.size();i++){
            lengthOfV1=lengthOfV1+Math.pow(v1.get(i).getTfidf(),2);
            lengthOfV2=lengthOfV2+Math.pow(v2.get(i).getTfidf(),2);
            dotproduct=dotproduct+(v1.get(i).getTfidf()*v2.get(i).getTfidf());
        }
        lengthOfV1=Math.sqrt(lengthOfV1);
        lengthOfV2=Math.sqrt(lengthOfV2);
        double result=(dotproduct)/(lengthOfV1*lengthOfV2);
        //System.out.println(dotproduct+" "+lengthOfV1+" "+lengthOfV2);
        return  result;
    }

    public static void main(String[] args) {
        Cosine cosine=new Cosine();
        ArrayList<Gram> doc1=cosine.readFile("d1.txt");
        ArrayList<Gram> doc2=cosine.readFile("d2.txt");
        ArrayList<Gram> doc3=cosine.readFile("d3.txt");
        ArrayList<ArrayList<Gram>> documents = new ArrayList<ArrayList<Gram>>();
        documents.add(doc1);
        documents.add(doc2);
        documents.add(doc3);

        for(int i=0;i<doc1.size();i++){
            double temp=cosine.tfIdf(doc1,documents,doc1.get(i).getWord());
            doc1.get(i).setTfidf(temp);
        }
        //Collections.sort(doc1, new GramComparatorTFIDF());


        for(int i=0;i<doc2.size();i++){
            double temp=cosine.tfIdf(doc2,documents,doc2.get(i).getWord());
            doc2.get(i).setTfidf(temp);
        }
        //Collections.sort(doc2, new GramComparatorTFIDF());

        for(int i=0;i<doc3.size();i++){
            double temp=cosine.tfIdf(doc3,documents,doc3.get(i).getWord());
            doc3.get(i).setTfidf(temp);
        }
        //Collections.sort(doc3, new GramComparatorTFIDF());

        ArrayList<String> bog=cosine.BagOfWords(documents);
        ArrayList<Gram> v1=cosine.getVector(bog,doc1);
        ArrayList<Gram> v2=cosine.getVector(bog,doc2);
        ArrayList<Gram> v3=cosine.getVector(bog,doc3);

        System.out.println("Mức độ tương tự giữa văn bản 1 và 2: "+cosine.determineCosine(v1,v2));
        System.out.println("Mức độ tương tự giữa văn bản 1 và 3: "+cosine.determineCosine(v1,v3));
    }
}
