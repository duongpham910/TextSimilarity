package ParseHTML;

import Model.ItemNews;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by DuongPham on 26/07/2016.
 */
public class RssHandler extends DefaultHandler {

    public static final String ITEM = "item";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String LINK = "link";
    public static final String DATE = "pubdate";

    private ItemNews item;
    private ArrayList<ItemNews> itemList=new ArrayList<ItemNews>();
    public boolean flag=false;
    public StringBuilder sb=new StringBuilder();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if(localName.equalsIgnoreCase(ITEM)){
            item=new ItemNews();
            flag=true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if(localName.equalsIgnoreCase(ITEM)){
            itemList.add(item);
        }else if(localName.equalsIgnoreCase(TITLE) && flag==true){
            item.setTitle(sb.toString());
        }else if(localName.equalsIgnoreCase(DESCRIPTION) && flag==true){
            item.setDescription(sb.toString());
        }else if(localName.equalsIgnoreCase(LINK) && flag==true){
            item.setLink(sb.toString());
        }else if(localName.equalsIgnoreCase(DATE) && flag==true){
            item.setDate(sb.toString());
        }
        sb=new StringBuilder();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if(flag && sb!=null){
            sb.append(ch,start,length);
        }
    }

    public ArrayList<ItemNews> getItemList() {
        return itemList;
    }
}
