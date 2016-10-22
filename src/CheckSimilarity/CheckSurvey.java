package CheckSimilarity;

import DAO.PatternDAO;
import Model.ArticlePattern;
import Model.ItemNews;
import Ouf_Of_PlaceSimlarity.OutOfPlace;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DuongPham on 28/08/2016.
 */
public class CheckSurvey {



    public static void main(String[]args){
        PatternDAO patternDAO=new PatternDAO();
        OutOfPlace outOfPlace=new OutOfPlace();
        List<ArticlePattern> patternList=patternDAO.getListPattern(44);
        for(int i=0;i<patternList.size();i++){
            ArticlePattern articlePattern=patternList.get(i);
            List<ItemNews> itemList=new ArrayList<ItemNews>();
            itemList.add(articlePattern.getSample());
            itemList.add(articlePattern.getDoc1());
            itemList.add(articlePattern.getDoc2());
            System.out.println("-------Patten"+(i+1)+"-------");
            outOfPlace.showResult(itemList);
        }
    }
}
