package ParseHTML;



import Model.ItemNews;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;

/**
 * Created by DuongPham on 26/07/2016.
 */
public class RssParser {
    public ArrayList<ItemNews> getNewList(String link){
        ArrayList<ItemNews> listItem=new ArrayList<ItemNews>();
        try {
            //URL url=new URL(link);
            DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder=documentBuilderFactory.newDocumentBuilder();
            Document document=documentBuilder.parse(link);
            XPath xPath= XPathFactory.newInstance().newXPath();
            NodeList nodeList= (NodeList) xPath.compile("//item").evaluate(document, XPathConstants.NODESET);
            for(int i=0;i<nodeList.getLength();i++){
                ItemNews item=new ItemNews();
                item.setTitle(xPath.compile("./title").evaluate(nodeList.item(i),XPathConstants.STRING).toString());
                item.setDescription(xPath.compile("./description").evaluate(nodeList.item(i),XPathConstants.STRING).toString());
                item.setDate(xPath.compile("./pubDate").evaluate(nodeList.item(i), XPathConstants.STRING).toString());
                item.setLink(xPath.compile("./link").evaluate(nodeList.item(i),XPathConstants.STRING).toString());
                listItem.add(item);
            }
            return listItem;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error");
            return null;
        }
    }
}
