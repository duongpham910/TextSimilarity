package ParseHTML;

import java.util.ArrayList;
import java.io.IOException;

import DAO.ItemDAO;
import Model.ItemNews;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Created by DuongPham on 26/07/2016.
 */
public class ImportData {

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

    public static String parseDAILYMAIL(String url){
        try {
            int index=url.indexOf("?ITO");
            url=url.substring(0,index);
            Document doc = Jsoup.connect(url).get();
            System.out.println("Title:"+doc.title());
            Elements content=doc.getElementsByClass("mol-para-with-font");
            StringBuilder sb=new StringBuilder();
            for(Element con: content){
                sb.append(con.text());
            }
            return String.valueOf(sb);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static String parseNEWYORK(String url){
        try {

            Document doc = Jsoup.connect(url).get();
            System.out.println("Title:"+doc.title());
            Elements content=doc.getElementsByClass("entry-content");
            StringBuilder sb=new StringBuilder();
            for(Element con: content){
                sb.append(con.select("p").text());
            }
            return String.valueOf(sb);
        } catch (IOException e) {
            System.out.println(url);
            e.printStackTrace();
            return null;
        }
    }
    public static String parseCHICAGO(String url){
        try {

            Document doc = Jsoup.connect(url).get();
            System.out.println("Title:"+doc.title());
            Elements content=doc.getElementsByClass("trb_ar_page");
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

    public static String parseDANTRI(String url){
        try {

            Document doc = Jsoup.connect(url).get();
            System.out.println("Title:"+doc.title());
            Element content=doc.getElementById("divNewsContent");
            //System.out.println(content.select("p").text());
            return String.valueOf(content.select("p").text());
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

    public static String parsePHAPLUAT(String url) {
        try {

            Document doc = Jsoup.connect(url).get();
            Element content = doc.getElementById("main-detail");
            // System.out.println(content.select("p").text());
            return String.valueOf(content.select("p").text());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static String parseVIETNAMNET(String url) {
        try {

            Document doc = Jsoup.connect(url).get();
            Element content = doc.getElementById("ArticleContent");
            // System.out.println(content.select("p").text());
            return String.valueOf(content.select("p").text());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static String parseCONGAN(String url) {
        try {

            Document doc = Jsoup.connect(url).get();
            Element content = doc.getElementById("links");
            String sb=content.select("p").text();
            return String.valueOf(sb);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static String parseBONGDA(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements content = doc.select("div[class=\"exp_content\"]");
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

    public static String parseTIENPHONG(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            System.out.println(doc.title());
            Element content = doc.getElementById("article-body");
            String sb=content.text();
            return String.valueOf(sb);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static void executeParse(String[] link,String namePublisher){
        for(int index=0;index<link.length;index++){
            ArrayList<ItemNews> items=parser.getNewList(link[index]);
            System.out.println(items.size()+" "+namePublisher);
            ItemDAO idao=new ItemDAO();
            for(int i=0;i<items.size();i++){
                ItemNews it=items.get(i);
                String content="";
                if(namePublisher.equals("VNEXPRESS")){
                    if(notEmpty(it.getLink())){
                        content=parseVNEXPRESS(it.getLink());
                        it.setContent(content);
                        idao.addItem(it, "VNEXPRESS");
                    }
                }else if(namePublisher.equals("TUOITRE")){
                    content=parseTUOITRE(it.getLink());
                    it.setContent(content);
                    idao.addItem(it, "TUOITRE");
                }
                else if(namePublisher.equals("DANTRI")){
                    content=parseDANTRI(it.getLink());
                    it.setContent(content);
                    idao.addItem(it, "DANTRI");
                }else if(namePublisher.equals("LAODONG")){
                    content=parseLAODONG(it.getLink());
                    it.setContent(content);
                    idao.addItem(it,"LAODONG");
                }else if(namePublisher.equals("VIETNAMNET")){
                    content=parseVIETNAMNET(it.getLink());
                    it.setContent(content);
                    idao.addItem(it,"VIETNAMNET");
                } else if(namePublisher.equals("CONGAN")){
                    content=parseCONGAN(it.getLink());
                    it.setContent(content);
                    idao.addItem(it,"CONGAN");
                } else if(namePublisher.equals("PHAPLUAT")){
                    content=parsePHAPLUAT(it.getLink());
                    it.setContent(content);
                    idao.addItem(it,"PHAPLUAT");
                } else if(namePublisher.equals("TIENPHONG")){
                    content=parseTIENPHONG(it.getLink());
                    it.setContent(content);
                    idao.addItem(it,"TIENPHONG");
                }else if(namePublisher.equals("CHICAGO")){
                    content=parseCHICAGO(it.getLink());
                    it.setContent(content);
                    idao.addItemEN(it, "CHICAGO");
                }else if(namePublisher.equals("DAILYMAIL")){
                    content=parseDAILYMAIL(it.getLink());
                    it.setContent(content);
                    idao.addItemEN(it, "DAILYMAIL");
                }else if(namePublisher.equals("NEWYORK")){
                    content=parseNEWYORK(it.getLink());
                    it.setContent(content);
                    idao.addItemEN(it, "NEWYORK");
                }
            }
        }
    }

    public static boolean notEmpty(String string) {
        if (string == null || string.length() == 0){
            return false;
        }else{
            return true;
        }

    }

    public static void main(String[] args) {
        parser=new RssParser();
        //executeParse(Variables.VNEXPRESS_LINKS,"VNEXPRESS");
        //executeParse(Variables.DANTRI_LINKS,"DANTRI");
        //executeParse(Variables.TUOITRE_LINKS,"TUOITRE");
        //executeParse(Variables.LAODONG_LINKS,"LAODONG");
        executeParse(Variables.VIETNAMNET_LINKS,"VIETNAMNET");
        executeParse(Variables.CONGAN_LINKS,"CONGAN");
        executeParse(Variables.PHAPLUAT_LINKS,"PHAPLUAT");
        executeParse(Variables.TIENPHONG_LINKS,"TIENPHONG");
        //executeParse(Variables.CHICAGO_LINKS,"CHICAGO");
        //executeParse(Variables.DAILYMAIL_LINKS,"DAILYMAIL");
        //executeParse(Variables.NEWYORK_LINKS,"NEWYORK");
    }
}
