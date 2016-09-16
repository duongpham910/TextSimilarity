package DAO;

import Model.ItemNews;
import Model.ArticlePattern;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DuongPham on 28/08/2016.
 */
public class PatternDAO {

    private Connection con;

    public PatternDAO(){
        ConnectionDB connectionDB=ConnectionDB.getInstance();
        con=connectionDB.getCon();
    }

    public List<ArticlePattern> getListPattern(){
        List<ArticlePattern> l=new ArrayList<ArticlePattern>();
        String sql="SELECT * FROM pattern";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                ArticlePattern ap=new ArticlePattern();
                ap.setId(rs.getInt(1));
                ap.setSample(getItem(rs.getInt(2)));
                ap.setDoc1(getItem(rs.getInt(3)));
                ap.setDoc2(getItem(rs.getInt(4)));
                ap.setPart(rs.getInt(5));
                l.add(ap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    public ItemNews getItem(int id){
        ItemNews it=new ItemNews();
        String sql="SELECT * FROM article_vn WHERE id=?";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                it.setId(id);
                it.setTitle(rs.getString(2));
                it.setDescription(rs.getString(3));
                it.setLink(rs.getString(4));
                it.setDate(rs.getString(5));
                it.setContent(rs.getString(6));
                it.setPublissher(rs.getString(7));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return it;
    }

    public List<ArticlePattern> getListPattern(int index){
        List<ArticlePattern> l=new ArrayList<ArticlePattern>();
        String sql="SELECT * FROM pattern WHERE part=?";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setInt(1,index);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                ArticlePattern ap=new ArticlePattern();
                ap.setId(rs.getInt(1));
                ap.setSample(getItem(rs.getInt(2)));
                ap.setDoc1(getItem(rs.getInt(3)));
                ap.setDoc2(getItem(rs.getInt(4)));
                ap.setPart(rs.getInt(5));
                l.add(ap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

}
