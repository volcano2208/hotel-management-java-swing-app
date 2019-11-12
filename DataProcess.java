/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Entity.Room;
import Entity.TypeRoom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kakap
 */
public class DataProcess {
    private static final String USERNAME="root";
    private static final String PASSWORD="root";
    private static final String CONN_STRING="jdbc:mysql://localhost:8080/fptcompany";
           public Connection getConnection()
           {
            Connection conn = null;
               try {
                   conn = DriverManager.getConnection(USERNAME,PASSWORD,CONN_STRING);
                   System.out.println("connected");
                   Statement st = (Statement) conn.createStatement();
               } catch (SQLException e) {
                   System.out.println(e);
               }
               return conn;
           }
           public  boolean  addnewRoom(String id,float price,String idtyperoom){
          int result = 0;
          Connection conn = getConnection();
          String sql = "INSERT INTO tblRoom  VALUES (?,?,?)";
         try {
              try (PreparedStatement prst = conn.prepareStatement(sql)) {
                  prst.setString(1,id );
                  prst.setFloat(2, price);
                  prst.setString(3, idtyperoom);
                  result = prst.executeUpdate();
              }
             conn.close();
          
         } catch (SQLException ex) {
             Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
         }
     return result >0;
     }
           public boolean  deleteRoom(String id){
     int result =0 ; 
     Connection conn = getConnection();
     String sql = "DELETE FROM tblRoom WHERE _id=?";
     PreparedStatement prst;
         try {
             prst = conn.prepareStatement(sql);
             prst.setString(1, id);
             result = prst.executeUpdate();
             prst.close();
             conn.close();
         } catch (SQLException ex) {
             Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
         }
     return result >0;
     }
           public boolean  updateRoom(String id,float price,String typeroom){
     int result = 0 ;
     Connection conn = getConnection();
     String sql = "UPDATE tblRoom SET _price=?,_idtyperoom=? WHERE _id=?";
         try {
             PreparedStatement prst = conn.prepareStatement(sql);
             prst.setString(3, id);
             prst.setFloat(1, price);
             prst.setString(2, typeroom);
             result = prst.executeUpdate();
             prst.close();
             prst.close();
         } catch (SQLException ex) {
             Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
         }
     return result >0;
     }
           public ArrayList<Room> getRoom()
       {
           ArrayList<Room> listRoom = new ArrayList<>();
           Connection conn=getConnection();
           String sql = "SELECT * FROM tblRoom ";
           Statement st;
           try {
               st = conn.createStatement();
               try (ResultSet rs = st.executeQuery(sql)) {
                   while (rs.next()){
                       Room r = new Room();
                       r.setId(rs.getString(1));
                       r.setPrice(rs.getFloat(2));
                       r.setTyperoom(rs.getString(3));
                       listRoom.add(r);
                   }
               }
               st.close();
               conn.close();
           } catch (SQLException ex) {
               Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
           }
          return listRoom;
       }
           public Room getByID(String id)
       {
           Room r=null;
           Connection conn=getConnection();
           String sql = "SELECT * FROM tblRoom WHERE _id='"+id+"'";
           Statement st;
           try {
               st = conn.createStatement();
               try (ResultSet rs = st.executeQuery(sql)) {
                   while (rs.next()){
                       r = new Room();
                       r.setId(rs.getString(1));
                       r.setPrice(rs.getFloat(2));
                       r.setTyperoom(rs.getString(3));
                       //list2.add(p);
                   }
               }
               st.close();
               conn.close();
           } catch (SQLException ex) {
               Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
           }
          return r;
       }
           public ArrayList<Room>  searchbyname(String id){
           ArrayList<Room> listR = new ArrayList<>();
           Connection conn=getConnection();
           String sql = "SELECT * FROM tblRoom WHERE _id LIKE '%"+id+"%'";
           Statement st;
           try {
               st = conn.createStatement();
               try (ResultSet rs = st.executeQuery(sql)) {
                   while (rs.next()){
                       Room r = new Room();
                       r.setId(rs.getString(1));
                       r.setPrice(rs.getFloat(2));
                       r.setTyperoom(rs.getString(3));
                       listR.add(r);
                   }
               }
               st.close();
               conn.close();
           } catch (SQLException ex) {
               Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
           }
          return listR;
       }
           public ArrayList<TypeRoom> getTypeRoom()
       {
           ArrayList<TypeRoom> listTypeRoom = new ArrayList<>();
           Connection conn=getConnection();
           String sql = "SELECT * FROM tblTypeRoom ";
           Statement st;
           try {
               st = conn.createStatement();
               try (ResultSet rs = st.executeQuery(sql)) {
                   while (rs.next()){
                       TypeRoom t = new TypeRoom();
                       t.setId(rs.getString(1));
                       t.setName(rs.getString(2));
                       listTypeRoom.add(t);
                   }
               }
               st.close();
               conn.close();
           } catch (SQLException ex) {
               Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
           }
          return listTypeRoom;
       }  
           public ArrayList<Room> getAvailableRoom (String typeroom,String datein,String dateout)
           {
               ArrayList<Room> listRoom = new ArrayList<>();
           Connection conn=getConnection();
           String sql = "SELECT *FROM tblRoom WHERE _id  IN (select _idroom from tblOrder WHERE  tblOrder.timein <'"+datein+"'and tblOder.timeout > '"+dateout+"'"
                       + "and _idtyperoom in (SELECT _name FROM tblTypeRoom WHERE _name ='"+typeroom+"')";
           Statement st;
           try {
               st = conn.createStatement();
                   try (ResultSet rs = st.executeQuery(sql)) {
                       while (rs.next()){
                           Room r = new Room();
                           r.setId(rs.getString(1));
                           r.setPrice(rs.getFloat(2));
                           r.setTyperoom(rs.getString(3));
                           listRoom.add(r);
                       }
                   }
               st.close();
               conn.close();
           } catch (SQLException ex) {
               Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
           }
          return listRoom; 
           }
           public boolean  addnewOrder(String id,String customerName,String dateofbirth,int identitycard,int phone)
           {
                   int result = 0;
          Connection conn = getConnection();
          String sql = "INSERT INTO tblOrder  VALUES (?,?,?,?,?)";
         try {
                       try (PreparedStatement prst = conn.prepareStatement(sql)) {
                           prst.setString(1,id );
                           prst.setString(2, customerName);
                           prst.setString(3, dateofbirth);
                           prst.setInt(3, identitycard);
                           prst.setInt(4, phone);
                           result = prst.executeUpdate();
                       }
             conn.close();
         } catch (SQLException ex) {
             Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
         }
     return result >0;
           }
}
