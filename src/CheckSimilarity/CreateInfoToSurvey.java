package CheckSimilarity;

import DAO.ItemDAO;
import Model.ItemNews;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by DuongPham on 23/08/2016.
 */
public class CreateInfoToSurvey {

    public static String parseHeadingDANTRI(String url) {
        try {

            Document doc = Jsoup.connect(url).get();
            Element content = doc
                    .getElementById("ctl00_IDContent_ctl00_divContent");
            if(!content.select("h2").select("span").text().equals(null)){
                content.select("h2").select("span").remove();
            }
            if(!content.select("h2").select("a").text().equals(null)){
                content.select("h2").select("a").remove();
            }
            String s = content.select("h2").text();

            return String.valueOf(s);
        } catch (Exception e) {
            return String.valueOf(e);
        }
    }

    public static String parseHeadingVNEXPRESS(String url){
        try {

            Document doc = Jsoup.connect(url).get();
            Elements content = doc.select("h3[class=\"short_intro txt_666\"]");
            StringBuilder sb = new StringBuilder();
            for (Element con : content) {
                sb.append(con.text());
            }
            String temp=String.valueOf(sb);
            if( temp.equals("")){
                content = doc.select("div[class=\"short_intro txt_666\"]");
                for (Element con : content) {
                    sb.append(con.text());

                }
            }
            return String.valueOf(sb);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static String parseHeadingTUOITRE(String url){
        try {

            Document doc = Jsoup.connect(url).get();
            Elements content = doc.select("p[class=\"txt-head\"]");
            StringBuilder sb = new StringBuilder();
            for (Element con : content) {
                sb.append(con.text());

            }
            int index = sb.indexOf("-");
            String temp=sb.substring(index+1);
            return temp;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    public static String parseHeadingLAODONG(String url){
        try {

            Document doc = Jsoup.connect(url).get();
            Elements content = doc.select("div[class=\"content\"]");
            StringBuilder sb = new StringBuilder();
            for (Element con : content) {
                sb.append(con.select("p[class=\"sapo\"]").text());
                sb.append(con.select("p[class=\"sapo cms-desc\"]").text());
            }
            return String.valueOf(sb);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[]args){
        ItemDAO id=new ItemDAO();
        ArrayList<ItemNews> listItem=id.getListArticleSimilaryty("Thanh Hóa");
         String heading="";
        String description="";
        int count=0;
        for(int i=0;i<listItem.size();i++){
            ItemNews inews=listItem.get(i);
            if(listItem.get(i).getContent()== null || listItem.get(i).getContent().equals("")){

            }else {
                count++;

                description= inews.getTitle();
                System.out.println(inews.getId() + " " + description);


                if(inews.getPublissher().equals("VNEXPRESS")){
                    heading=parseHeadingVNEXPRESS(inews.getLink());
                }else if(inews.getPublissher().equals("DANTRI")){
                    heading=parseHeadingDANTRI(inews.getLink());
                    //heading="";
                }else if(inews.getPublissher().equals("LAODONG")){
                    heading=parseHeadingLAODONG(inews.getLink());
                }else{
                    heading=parseHeadingTUOITRE(inews.getLink());
                }
                System.out.println(heading);
                System.out.println();
            }
        }
//        try {
//            OutputStream output = new FileOutputStream("documentTesting.txt");
//            PrintStream printOut = new PrintStream(output);
//            System.setOut(printOut);
//            System.out.println(count + " " + description);
//            System.out.println(heading);
//        }catch (IOException e){
//            System.out.println(e);
//        }
    }
}
