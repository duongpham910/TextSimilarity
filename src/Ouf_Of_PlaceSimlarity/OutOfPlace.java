package Ouf_Of_PlaceSimlarity;

import DAO.ItemDAO;
import Model.Gram;
import Model.GramComparatorTFIDF;
import Model.ItemNews;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Created by DuongPham on 31/07/2016.
 */
public class OutOfPlace {


    public String Filter(String tam){
        String text = tam.replaceAll("\\`|\\~|\\!|\\@|\\#|\\$|\\%|\\^|\\&|\\*|\\(|"
                + "\\)|\\_|\\=|\\+|\\{|\\[|\\}|\\]|\\<|\\,|\\>|\\.|\\?|"
                + "\\/|\\;|\\:|\\–|\\d+", "");
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
        int N = 1; // uni-gram
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

    //Tinh theo TF-IDF
    public double computeDistance(ArrayList<Gram> doc1,ArrayList<Gram> doc2) {
        double result=0;
        for(int i=0;i<5;i++) {
            if(doc1.get(i).getWord().equals(doc2.get(i).getWord())){
                result=result+ Math.abs(doc1.get(i).getTfidf()-doc2.get(i).getTfidf());
            }else{
                result=result+doc1.get(i).getTfidf();
            }
        }
        return result;
    }
    //Tinh theo Out-of-place
    public double computeDistance1(ArrayList<Gram> doc1,ArrayList<Gram> doc2) {
        int DistanceMeasure = 0;
        int max = 0;
        int count = 0;
        for (int i = 0; i < doc2.size(); i++) {
            Gram g = doc2.get(i);
            boolean flag = true;
            for (int j = 0; j < doc1.size(); j++) {
                Gram temp = doc1.get(j);
                if (g.getWord().equals(temp.getWord())) {
                    int pos = Math.abs(j - i);
                    flag = false;
                    DistanceMeasure = DistanceMeasure + pos;
                    break;
                }
            }
            if (flag == true) {
                count++;
            }
        }
        if(doc1.size()>doc2.size()){
            max=doc1.size();
        }else{
            max=doc2.size();
        }
        DistanceMeasure = DistanceMeasure + count * max;
        return DistanceMeasure;
    }

    public double precentSimilarity(int size1,int size2,double distance){
        int maxDistance=0;
        if(size1>size2){
            maxDistance=size1*size2;
        }else{
            maxDistance=size2*size2;
        }
        double result=(((maxDistance)-distance)/(maxDistance))*100;
        return result;
    }

    public void showResult(List<ItemNews> listItem){
        ArrayList<ArrayList<Gram>> documents = new ArrayList<ArrayList<Gram>>();
        ArrayList<Gram> listGram;
        for(int i=0;i<listItem.size();i++){
            listGram=new ArrayList<Gram>();
            if(listItem.get(i).getContent()==null || listItem.get(i).getContent().equals("")){

            }else{
                listGram=readText(listItem.get(i).getContent());
                documents.add(listGram);
            }
        }

        for(int index=0;index<documents.size();index++){
            listGram=documents.get(index);
            for (int j=0;j<listGram.size();j++){
                double temp=tfIdf(listGram, documents, listGram.get(j).getWord());
                documents.get(index).get(j).setTfidf(temp);
            }
            Collections.sort(documents.get(index), new GramComparatorTFIDF());
        }

        for(int i=0;i<documents.size();i++){
            double distance=computeDistance1(documents.get(0), documents.get(i));
            double similarity=precentSimilarity(documents.get(0).size(),documents.get(i).size(),distance);
            System.out.println("1 vs "+(i+1)+": "+similarity );
        }
    }

    public static void main(String[] args) {
        OutOfPlace oop=new OutOfPlace();
        ItemDAO iDAO=new ItemDAO();
        ArrayList<ArrayList<Gram>> documents = new ArrayList<ArrayList<Gram>>();
        ArrayList<ItemNews> listItem=iDAO.getListArticleSimilaryty("Bão");
        //ArrayList<ItemNews> listItem=iDAO.getListArticleSimilarytyEN("");
        oop.showResult(listItem);
    }
}
