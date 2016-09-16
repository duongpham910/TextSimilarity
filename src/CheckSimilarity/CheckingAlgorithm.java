package CheckSimilarity;

import DAO.ItemDAO;
import Model.Gram;
import Model.GramComparatorTFIDF;
import Model.ItemNews;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by DuongPham on 05/08/2016.
 */
public class CheckingAlgorithm {

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

    public double checkingWord(ArrayList<Gram> doc1,ArrayList<Gram> doc2){
        double dem=0;
        for (int i = 0; i < doc2.size(); i++) {
            Gram g = doc2.get(i);
            for (int j = 0; j < doc1.size(); j++) {
                Gram temp = doc1.get(j);
                if (g.getWord().equals(temp.getWord())) {
                    dem++;
                }
            }
        }
        double result=(dem/doc2.size())*100;
        return  result;
    }

    public static void main(String[] args) {
        CheckingAlgorithm check=new CheckingAlgorithm();
        ItemDAO iDAO=new ItemDAO();

        /* Kiem tra tu file*/
//        ArrayList<Gram> doc1=check.readFile("d1.txt");
//        ArrayList<Gram> doc2=check.readFile("d2.txt");
//        ArrayList<Gram> doc3=check.readFile("d3.txt");
//        ArrayList<ArrayList<Gram>> documents = new ArrayList<ArrayList<Gram>>();
//        documents.add(doc1);
//        documents.add(doc2);
//        documents.add(doc3);
//
//        for(int i=0;i<doc1.size();i++){
//            double temp=check.tfIdf(doc1,documents,doc1.get(i).getWord());
//            doc1.get(i).setTfidf(temp);
//        }
//        Collections.sort(doc1, new GramComparatorTFIDF());
//
//
//        for(int i=0;i<doc2.size();i++){
//            double temp=check.tfIdf(doc2,documents,doc2.get(i).getWord());
//            doc2.get(i).setTfidf(temp);
//        }
//        Collections.sort(doc2, new GramComparatorTFIDF());
//
//        for(int i=0;i<doc3.size();i++){
//            double temp=check.tfIdf(doc3,documents,doc3.get(i).getWord());
//            doc3.get(i).setTfidf(temp);
//        }
//        Collections.sort(doc3, new GramComparatorTFIDF());

        /*Kiem tra tu CSDL*/
        ArrayList<ArrayList<Gram>> documents = new ArrayList<ArrayList<Gram>>();
        ArrayList<ItemNews> listItem=iDAO.getListArticleSimilaryty("");
        //ArrayList<ItemNews> listItem=iDAO.getListArticleSimilarytyEN("");
        ArrayList<Gram> listGram;
        for(int i=0;i<listItem.size();i++){
            listGram=new ArrayList<Gram>();
            if(listItem.get(i).getContent()==null || listItem.get(i).getContent().equals("")){

            }else{
                listGram=check.readText(listItem.get(i).getContent());
                documents.add(listGram);
            }
        }

        for(int index=0;index<documents.size();index++){
            listGram=documents.get(index);
            for (int j=0;j<listGram.size();j++){
                double temp=check.tfIdf(listGram,documents,listGram.get(j).getWord());
                documents.get(index).get(j).setTfidf(temp);
            }
            Collections.sort(documents.get(index), new GramComparatorTFIDF());
        }


        for(int i=0;i<documents.size();i++){
            double similarity=check.checkingWord(documents.get(0), documents.get(i));
            System.out.println((i+1)+": "+similarity+"%");
        }

    }
}
