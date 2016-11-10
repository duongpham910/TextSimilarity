package View;

import Algorithms.Out_of_place;
import Model.Gram;
import Model.GramComparatorTFIDF;
import Reader.PDF_reader;
import Reader.Text_reader;
import Reader.Word_reader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.text.*;

/**
 * Created by Duong Pham on 10/22/2016.
 */
public class Home extends JFrame implements ActionListener {

    public String original_text = "Original text";
    public String altered_text = "Rewrite text";
    public JButton btnSelectOriginal = new JButton("Original document");
    public JButton btnSelectAltered = new JButton("Altered (rewrite) copy");
    public JButton btnCheck = new JButton("Check");
    public JFileChooser fileopen = new JFileChooser();
    public Out_of_place out_of_place = new Out_of_place();
    public ArrayList<Gram> lOriginal;
    public ArrayList<Gram> lAltered;

    public JTextPane tpOriginal = new JTextPane();
    public JTextPane tpAltered = new JTextPane();
    public JTextPane tpResult =new JTextPane();

    public JLabel txtOriginal=new JLabel("");
    public JLabel txtAltered=new JLabel("");

    public ArrayList<Gram> list_gram_similar = new ArrayList<Gram>();
    public ArrayList<Gram> list_gram_similar_alt = new ArrayList<Gram>();

    private Highlighter.HighlightPainter cyanPainter;
    private Highlighter.HighlightPainter redPainter;

    public String temp_document;
    public StringBuilder sbResult;

    public int tag_counter=1;

    public int[] positions_highlight ;
    public int[] positions_highlight_altered;

    public StyledDocument doc_altered;
    public StyledDocument doc_origin;
    public Document temp_blank = new DefaultStyledDocument();



    public Home(){
        setSize(1920,1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p1=new JPanel();
        btnSelectOriginal.addActionListener(this);
        btnSelectAltered.addActionListener(this);
        btnCheck.addActionListener(this);
        p1.add(btnSelectOriginal);
        p1.add(btnSelectAltered);
        p1.add(btnCheck);

        JPanel p2=new JPanel(new GridLayout(0,2));
        p2.add(new JScrollPane(tpOriginal));
        p2.add(new JScrollPane(tpAltered));

        fileopen.setDialogTitle("Choose your file!");

        GridBagLayout gridBagLayout=new GridBagLayout();
        GridBagConstraints gridBagConstraints=new GridBagConstraints();

        JPanel p3=new JPanel();
        p3.setLayout(gridBagLayout);
        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=1;
        gridBagConstraints.gridwidth=2;
        gridBagConstraints.insets=new Insets(5,5,5,5);
        JScrollPane jp=new JScrollPane(tpResult);
        jp.setPreferredSize(new Dimension(1280, 150));
        p3.add(jp,gridBagConstraints);

        gridBagConstraints=new GridBagConstraints();
        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=0;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        p3.add(txtOriginal,gridBagConstraints);

        gridBagConstraints=new GridBagConstraints();
        gridBagConstraints.gridx=1;
        gridBagConstraints.gridy=0;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        p3.add(txtAltered,gridBagConstraints);

        cyanPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.cyan);
        redPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.red);

        add(p1,"North");
        add(p2,BorderLayout.CENTER);
        add(p3,"South");
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==btnSelectOriginal){
            tpOriginal.setText("waiting until process finish");
            fileopen.showOpenDialog(null);
            lOriginal = getListGram(fileopen.getSelectedFile().getAbsolutePath());
            txtOriginal.setText(fileopen.getSelectedFile().getName());
            tpOriginal.setText(temp_document);
            original_text=temp_document;
            positions_highlight=new int[original_text.length()];
            doc_origin = tpOriginal.getStyledDocument();
        }else if(e.getSource()==btnSelectAltered){
            tpAltered.setText("waiting until process finish");
            fileopen.showOpenDialog(null);
            lAltered = getListGram(fileopen.getSelectedFile().getAbsolutePath());
            txtAltered.setText(fileopen.getSelectedFile().getName());
            tpAltered.setText(temp_document);
            altered_text=temp_document;
            positions_highlight_altered=new int[altered_text.length()];
            doc_altered= tpAltered.getStyledDocument();
        }else if(e.getSource()==btnCheck){
            long start = System.currentTimeMillis();
            sbResult = new StringBuilder();
            checkSimilarity();
            double result=0;
            tpAltered.setDocument(temp_blank);
            tpOriginal.setDocument(temp_blank);
            for(int i=10;i>=5;i--){
                getListGramSimilar(i);
                //result=checkSimilarity(i);
                //System.out.println(i+"-gram similarity: "+result);
                setHighLightSimilarity();
            }
            insert_index();
            tpAltered.setDocument(doc_altered);
            tpOriginal.setDocument(doc_origin);
            tpResult.setText(String.valueOf(sbResult));
            System.out.println("Time to update = " +
                    (System.currentTimeMillis() - start));
        }
    }


    public ArrayList<Gram> getListGram(String file_path){
        String file_type=check_file_type(file_path);
        String text="";
        switch (file_type){
            case "doc":
                text = readDoc(file_path);
                break;
            case "docx":
                text = readDocx(file_path);
                break;
            case "pdf":
                text = readPdf(file_path);
                break;
            case "txt":
                text = readText(file_path);
                break;
            default:
                break;
        }
        temp_document=text;
        return out_of_place.createGram(text);
    }

    public String check_file_type(String file_path){
        int index=file_path.indexOf(".");
        String file_type=file_path.substring(index+1);
        return file_type;
    }

    public String readText(String file_path){
        Text_reader tr=new Text_reader();
        String result = tr.readFileText(file_path);
        return result;
    }

    public String readDoc(String file_path){
        Word_reader wr=new Word_reader();
        String result =wr.readDocFile(file_path);
        return  result;
    }

    public String readDocx(String file_path){
        Word_reader wr=new Word_reader();
        String result =wr.readDocxFile(file_path);
        return  result;
    }

    public String readPdf(String file_path){
        PDF_reader pr=new PDF_reader();
        pr.setFilePath(file_path);
        String result="";
        try {
            result = pr.ToText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void checkSimilarity() {
        ArrayList<ArrayList<Gram>> documents = new ArrayList<ArrayList<Gram>>();
        documents.add(lOriginal);
        documents.add(lAltered);

        for(int i=0;i<lOriginal.size();i++){
            double temp=out_of_place.tfIdf(lOriginal,documents,lOriginal.get(i).getWord());
            lOriginal.get(i).setTfidf(temp);
        }
        Collections.sort(lOriginal, new GramComparatorTFIDF());


        for(int i=0;i<lAltered.size();i++){
            double temp=out_of_place.tfIdf(lAltered,documents,lAltered.get(i).getWord());
            lAltered.get(i).setTfidf(temp);
        }
        Collections.sort(lAltered, new GramComparatorTFIDF());
        double d12=out_of_place.computeDistance1(lOriginal,lAltered);
        sbResult.append("Mức độ tương tự giữa văn bản 1 và 2: "+out_of_place.precentSimilarity(lOriginal.size(),lAltered.size(),d12));
        sbResult.append("\n");
    }

    private double checkSimilarity(int n_gram) {
        ArrayList<ArrayList<Gram>> documents = new ArrayList<ArrayList<Gram>>();
        documents.add(lOriginal);
        documents.add(lAltered);

        for(int i=0;i<lOriginal.size();i++){
            double temp=out_of_place.tfIdf(lOriginal,documents,lOriginal.get(i).getWord());
            lOriginal.get(i).setTfidf(temp);
        }
        Collections.sort(lOriginal, new GramComparatorTFIDF());


        for(int i=0;i<lAltered.size();i++){
            double temp=out_of_place.tfIdf(lAltered,documents,lAltered.get(i).getWord());
            lAltered.get(i).setTfidf(temp);
        }
        Collections.sort(lAltered, new GramComparatorTFIDF());
        double d12=out_of_place.computeDistance1(lOriginal,lAltered);
        return out_of_place.precentSimilarity(lOriginal.size(),lAltered.size(),d12);
    }

    public void getListGramSimilar(int k){
        lOriginal = out_of_place.createGram(original_text,k);
        lAltered = out_of_place.createGram(altered_text,k);
        for(int i=0;i<lOriginal.size();i++){
            String content=lOriginal.get(i).getWord();
            for(int j=0;j<lAltered.size();j++){
                if(content.equals(lAltered.get(j).getWord())){
                    list_gram_similar.add(lOriginal.get(i));
                    list_gram_similar_alt.add(lAltered.get(j));
                    break;
                }
            }
        }
        sbResult.append(k+"-gram (% tương đồng trong văn bản gốc): "+ precent_gram(list_gram_similar.size(),k));
        sbResult.append("\n");
    }

    public double precent_gram(int list_similar, int n_garm){
        String[] tokens = original_text.split("\\s+");
        double reuslt = ((list_similar * n_garm)/(double)tokens.length)*100;
        return reuslt;
    }




    public void setHighLightSimilarity(){
        for(int i=0;i<list_gram_similar.size();i++){
            String document=original_text;
            String word_in_gram1=list_gram_similar.get(i).getWord();
            int delete_index1 = word_in_gram1.indexOf(" ");
            int count=list_gram_similar.get(i).getCounter();
            int position=0;
            boolean flag=false;
            while(count!=0){
                int index = document.indexOf(word_in_gram1);
                if(!is_child_gram(position+index)){
                    positions_highlight[position+index]=tag_counter;
                    flag=true;
                }
                markHighLight(position+index,position+index+word_in_gram1.length(),1);
                position=position+index+delete_index1;
                document=document.substring(index+delete_index1);
                count--;
            }
            String document2=altered_text;
            String word_in_gram2=list_gram_similar_alt.get(i).getWord();
            int delete_index2 = word_in_gram2.indexOf(" ");
            int count2=list_gram_similar_alt.get(i).getCounter();
            int position2=0;
            while(count2!=0){
                int index = document2.indexOf(word_in_gram2);
                if(!is_child_gram_alter(position2+index)){
                    positions_highlight_altered[position2+index]=tag_counter;
                }
                markHighLight(position2+index,position2+index+word_in_gram2.length(),2);
                position2=position2+index+delete_index2;
                document2=document2.substring(index+delete_index2);
                count2--;
            }
            if(flag){
                tag_counter++;
            }
        }
    }


    public void insert_index(){
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, Color.RED);
        StyleConstants.setBackground(keyWord, Color.YELLOW);
        StyleConstants.setBold(keyWord, true);
        StyleConstants.setSuperscript(keyWord,true);

        for(int i=0;i<positions_highlight.length;i++){
            int tag_index=positions_highlight[i];
            if(positions_highlight[i]!=0 && i!=0){
                try {
                    if(tag_index<10){
                        doc_origin.remove(i-1,1);
                        doc_origin.insertString(i-1, String.valueOf(tag_index), keyWord );
                    }else if(tag_index>=10 && tag_index<100){
                        doc_origin.remove(i-2,2);
                        doc_origin.insertString(i-2, String.valueOf(tag_index), keyWord );
                    }else{
                        doc_origin.remove(i-3,3);
                        doc_origin.insertString(i-3, String.valueOf(tag_index), keyWord );
                    }
                } catch (BadLocationException e) {
                    System.out.println("Invalid location insert index");
                }
            }
        }

        for(int i=0;i<positions_highlight_altered.length;i++){
            int tag_index=positions_highlight_altered[i];
            if(positions_highlight_altered[i]!=0 && i!=0){
                try {
                    if(tag_index<10){
                        doc_altered.remove(i-1,1);
                        doc_altered.insertString(i-1, String.valueOf(tag_index), keyWord );
                    }else if(tag_index>=10 && tag_index<100){
                        doc_altered.remove(i-2,2);
                        doc_altered.insertString(i-2, String.valueOf(tag_index), keyWord );
                    }else{
                        doc_altered.remove(i-3,3);
                        doc_altered.insertString(i-3, String.valueOf(tag_index), keyWord );
                    }
                } catch (BadLocationException e) {
                    System.out.println("Invalid location insert index");
                }
            }
        }
    }

    public boolean is_child_gram(int position){
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setBackground(keyWord, Color.CYAN);
        Element e=doc_origin.getCharacterElement(position);
        AttributeSet attributeNew = e.getAttributes();
        if(attributeNew.isEqual(keyWord)){
            return true;
        }else{
            return false;
        }
    }

    public boolean is_child_gram_alter(int position){
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setBackground(keyWord, Color.CYAN);
        Element e=doc_altered.getCharacterElement(position);
        AttributeSet attributeNew = e.getAttributes();
        if(attributeNew.isEqual(keyWord)){
            return true;
        }else{
            return false;
        }
    }

    public void markHighLight(int start, int end, int type) {
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setBackground(keyWord, Color.CYAN);
        if(type==1){
            doc_origin.setCharacterAttributes(start, end-start, keyWord, false);
        }else{
            doc_altered.setCharacterAttributes(start, end-start, keyWord, false);
        }
    }

    public static void main(String[] args) {
        Home h=new Home();
    }
}
