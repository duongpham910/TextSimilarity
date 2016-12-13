package GenerateNgram;

import Model.Gram;
import Model.GramComparatorTFIDF;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by DuongPham on 10/04/2016.
 */
public class TF_IDF {


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
        int DistanceMeasure=0;
        int max=0;
        int count=0;
        for(int i=0;i<doc2.size();i++){
            Gram g=doc2.get(i);
            boolean flag=true;
            for(int j=0;j<doc1.size();j++){
                Gram temp=doc1.get(j);
                if(g.getWord().equals(temp.getWord())){
                    int pos=Math.abs(j-i);
                    flag=false;
//                    if(pos>max){
//                        max=pos;
//                    }
                    DistanceMeasure=DistanceMeasure+pos;
                    break;
                }
            }
            if(flag==true){
                count++;  //count word not match
            }
        }
        //System.out.println("max:"+max+" count:"+count);
        if(doc1.size()>doc2.size()){
            max=doc1.size();
        }else{
            max=doc2.size();
        }
        DistanceMeasure=DistanceMeasure+count*max;
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


    public static void main(String[] args) {
        TF_IDF ti=new TF_IDF();
        ArrayList<Gram> doc1=ti.readFile("d1.txt");
        ArrayList<Gram> doc2=ti.readFile("d2.txt");
        ArrayList<Gram> doc3=ti.readFile("d3.txt");
        ArrayList<ArrayList<Gram>> documents = new ArrayList<ArrayList<Gram>>();
        documents.add(doc1);
        documents.add(doc2);
        documents.add(doc3);

        for(int i=0;i<doc1.size();i++){
            double temp=ti.tfIdf(doc1,documents,doc1.get(i).getWord());
            doc1.get(i).setTfidf(temp);
        }
        Collections.sort(doc1, new GramComparatorTFIDF());


        for(int i=0;i<doc2.size();i++){
            double temp=ti.tfIdf(doc2,documents,doc2.get(i).getWord());
            doc2.get(i).setTfidf(temp);
        }
        Collections.sort(doc2, new GramComparatorTFIDF());

        for(int i=0;i<doc3.size();i++){
            double temp=ti.tfIdf(doc3,documents,doc3.get(i).getWord());
            doc3.get(i).setTfidf(temp);
        }
        Collections.sort(doc3, new GramComparatorTFIDF());

        for(int i=0;i<doc1.size();i++){
            System.out.println(doc1.get(i).getWord()+" "+doc1.get(i).getTfidf());
        }

        double d12=ti.computeDistance1(doc1,doc2);

        double d13=ti.computeDistance1(doc1,doc3);
        System.out.println(doc1.size()+" "+doc2.size()+" "+doc3.size());
        System.out.println("Mức độ tương tự giữa văn bản 1 và 2: "+ti.precentSimilarity(doc1.size(),doc2.size(),d12));
        System.out.println("Mức độ tương tự giữa văn bản 1 và 3: "+ti.precentSimilarity(doc1.size(),doc3.size(),d13));
    }


}
