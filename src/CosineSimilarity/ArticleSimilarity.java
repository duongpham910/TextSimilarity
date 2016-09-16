package CosineSimilarity;

import DAO.ItemDAO;
import Model.Gram;
import Model.ItemNews;

import java.util.ArrayList;

/**
 * Created by DuongPham on 29/07/2016.
 */
public class ArticleSimilarity {
    public static void main(String[] args) {
        ArticleSimilarity as=new ArticleSimilarity();
        ItemDAO iDAO=new ItemDAO();
        Cosine cosine=new Cosine();

        ArrayList<ArrayList<Gram>> documents = new ArrayList<ArrayList<Gram>>();
        ArrayList<ItemNews> listItem=iDAO.getListArticleSimilaryty("");
        //ArrayList<ItemNews> listItem=iDAO.getListArticleSimilarytyEN("woman");
        ArrayList<Gram> listGram;
        for(int i=0;i<listItem.size();i++){
            listGram=new ArrayList<Gram>();
            if(listItem.get(i).getContent()==null || listItem.get(i).getContent().equals("")){

            }else{
                listGram=cosine.readText(listItem.get(i).getContent());
                documents.add(listGram);
            }
        }

        for(int index=0;index<documents.size();index++){
            listGram=documents.get(index);
            for (int j=0;j<listGram.size();j++){
                double temp=cosine.tfIdf(listGram,documents,listGram.get(j).getWord());
                documents.get(index).get(j).setTfidf(temp);
            }
        }

        ArrayList<String> bog=cosine.BagOfWords(documents);
        ArrayList<ArrayList<Gram>> vectors = new ArrayList<ArrayList<Gram>>();
        ArrayList<Gram> v;
        for(int i=0;i<documents.size();i++){
            v=new ArrayList<Gram>();
            v=cosine.getVector(bog,documents.get(i));
            vectors.add(v);
        }
        for(int i=0;i<vectors.size();i++){
            System.out.println(" 1 và "+(i+1)+": "+cosine.determineCosine(vectors.get(0),vectors.get(i))*100);
        }
    }
}
