package DAO;

import Model.ItemNews;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by DuongPham on 27/07/2016.
 */
public class ItemDAO {
    private Connection con;


    public ItemDAO(){
       ConnectionDB connectionDB=ConnectionDB.getInstance();
        con=connectionDB.getCon();
    }

    public void addItem(ItemNews it,String publisher){
        String sql="INSERT INTO article_vn(title,description,link,pubdate,content,publisher) VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,it.getTitle());
            ps.setString(2,it.getDescription());
            ps.setString(3,it.getLink());
            ps.setString(4,it.getDate());
            ps.setString(5,it.getContent());
            ps.setString(6,publisher);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addItemEN(ItemNews it,String publisher){
        String sql="INSERT INTO article_en(title,description,link,pubdate,content,publisher) VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,it.getTitle());
            ps.setString(2,it.getDescription());
            ps.setString(3,it.getLink());
            ps.setString(4,it.getDate());
            ps.setString(5,it.getContent());
            ps.setString(6,publisher);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ItemNews> getListArticleSimilaryty(String pattern){
        ArrayList<ItemNews> l=new ArrayList<ItemNews>();
        String sql="SELECT * FROM article_vn WHERE title LIKE '%"+pattern+"%';";
        //String sql="SELECT * FROM article_vn WHERE id>879 AND title LIKE '%"+pattern+"%';";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                ItemNews it=new ItemNews();
                it.setId(rs.getInt(1));
                it.setTitle(rs.getString(2));
                it.setDescription(rs.getString(3));
                it.setLink(rs.getString(4));
                it.setDate(rs.getString(5));
                it.setContent(rs.getString(6));
                it.setPublissher(rs.getString(7));
                l.add(it);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    public ArrayList<ItemNews> getListArticleSimilarytyEN(String pattern){
        ArrayList<ItemNews> l=new ArrayList<ItemNews>();
        String sql="SELECT * FROM article_en WHERE title LIKE '%"+pattern+"%';";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                ItemNews it=new ItemNews();
                it.setId(rs.getInt(1));
                it.setTitle(rs.getString(2));
                it.setDescription(rs.getString(3));
                it.setLink(rs.getString(4));
                it.setDate(rs.getString(5));
                it.setContent(rs.getString(6));
                it.setPublissher(rs.getString(7));
                l.add(it);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    public ArrayList<ItemNews> getListArticleSimilaryty(String pattern,int index){
        ArrayList<ItemNews> l=new ArrayList<ItemNews>();
        String sql="SELECT * FROM article_vn WHERE id > ? AND title LIKE '%"+pattern+"%';";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setInt(1,index);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                ItemNews it=new ItemNews();
                it.setId(rs.getInt(1));
                it.setTitle(rs.getString(2));
                it.setDescription(rs.getString(3));
                it.setLink(rs.getString(4));
                it.setDate(rs.getString(5));
                it.setContent(rs.getString(6));
                it.setPublissher(rs.getString(7));
                l.add(it);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    public ItemNews getItem(int id){
        String sql="SELECT * FROM article_vn WHERE id = ? ";
        ItemNews it=new ItemNews();
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                it.setId(rs.getInt(1));
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

    public void updateItemNews(ItemNews it){
        String sql="UPDATE article_vn SET title=?,description=?,link=?,pubdate=?,content=? WHERE id=?";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,it.getTitle());
            ps.setString(2,it.getDescription());
            ps.setString(3,it.getLink());
            ps.setString(4,it.getDate());
            ps.setString(5,it.getContent());
            ps.setInt(6,it.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
