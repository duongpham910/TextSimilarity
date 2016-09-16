package CheckSimilarity;

import DAO.ItemDAO;
import Model.ItemNews;
import ParseHTML.RssParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Duong Pham on 9/16/2016.
 */
public class temp {

    private static RssParser parser;

    public static String parseVNEXPRESS(String url){
        try {
            Document doc = Jsoup.connect(url).get();
            System.out.println("Title:"+doc.title());
            Elements content=doc.select("p[class=Normal]");
            StringBuilder sb=new StringBuilder();
            for(Element con: content){
                //System.out.println(con.text());
                sb.append(con.text());
            }
            return String.valueOf(sb);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static String parseDANTRI(String url){
        try {
            Document doc = Jsoup.connect(url).get();
            System.out.println("Title:"+doc.title());
            Elements content = doc.select("div[id=\"divNewsContent\"]");
            StringBuilder sb = new StringBuilder();
            for (Element con : content) {
                sb.append(con.select("p").text());
            }
            return String.valueOf(sb);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }


    public static String parseTUOITRE(String url){
        try {

            Document doc = Jsoup.connect(url).get();
            System.out.println("Title:"+doc.title());
            Elements content=doc.getElementsByClass("fck");
            StringBuilder sb=new StringBuilder();
            for(Element con: content){
                sb.append(con.select("p").text());
            }
            return String.valueOf(sb);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    public static String parseLAODONG(String url) {
        try {

            Document doc = Jsoup.connect(url).get();
            System.out.println("Title:"+doc.title());
            Elements content = doc.select("div[class=\"content\"]");
            StringBuilder sb = new StringBuilder();
            for (Element con : content) {
                sb.append(con.select("p").text());
                if(!con.select("span").text().equals(null)){
                    con.select("span[class=\"cmt-count\"]").remove();
                    sb.append(con.select("span").text());
                }
            }
            return String.valueOf(sb);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[]args){

        for(int i=2031;i<=2031;i++){
            ItemDAO iDao=new ItemDAO();
            ItemNews it=iDao.getItem(i);
            String publiser=it.getPublissher();
//            if(publiser.equals("VNEXPRESS")){
//                String content=parseVNEXPRESS(it.getLink());
//                it.setContent(content);
//                iDao.updateItemNews(it);
//            }else if(publiser.equals("TUOITRE")){
//                String content=parseTUOITRE(it.getLink());
//                it.setContent(content);
//                iDao.updateItemNews(it);
//            }
//            else if(publiser.equals("DANTRI")){
//                String content=parseDANTRI(it.getLink());
//                it.setContent(content);
//                iDao.updateItemNews(it);
//            }else if(publiser.equals("LAODONG")){
//                String content=parseLAODONG(it.getLink());
//                it.setContent(content);
//                iDao.updateItemNews(it);
//            }

            if(publiser.equals("DANTRI")){
                String content=parseDANTRI(it.getLink());
                System.out.println(content);
                it.setContent(content);
                iDao.updateItemNews(it);
            }
        }
    }
}
