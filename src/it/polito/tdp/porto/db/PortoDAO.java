package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Paper;
import it.polito.tdp.porto.model.Pubblication;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public List<Author>getAutore() {

		final String sql = "SELECT * FROM author";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			

			ResultSet rs = st.executeQuery();
			
			List<Author> ltemp = new LinkedList<Author>(); 

			while (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				ltemp.add(autore);
			}

			return ltemp;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public List<Pubblication> getc(int id){
		final String sql = "select creator.`eprintid` from creator \n" + 
				"inner join author on creator.authorid = author.id\n" + 
				"where author.id = ?";
		List<Pubblication> mtemp = new LinkedList<Pubblication>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);
			int eprintid = 0;
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				eprintid = rs.getInt("eprintid");
				mtemp.add(new Pubblication(id, eprintid));
			}
			conn.close();
			return mtemp;
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public boolean getCoautori(int id1, int id2) {
		final String sql = "SELECT COUNT(*) AS conto FROM creator WHERE authorid=? OR authorid=? group by eprintid;";
		final String sql1 = "select * from creator where authorid=719";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id1);
			st.setInt(2, id2);
			
			boolean coautori = false;

			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				if(rs.getInt("conto")>1) {
					coautori = true;
					break;
				}
			}
			conn.close();
			return coautori;
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public void createTmpTable() {
		final String sql = "create table eprintids(eprintid tinytext) ";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			int rs = st.executeUpdate();
			conn.close();
		}catch(SQLException e) {
			 e.printStackTrace();
				throw new RuntimeException("Errore Db");
		}
	}
	
	public void InsertIntoTmpTable(Author a) {
		final String sql = "insert into eprintids select eprintid from creator where authorid = ?;";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, a.getId());
			int rs = st.executeUpdate();
			conn.close();
		}catch(SQLException e) {
			 e.printStackTrace();
				throw new RuntimeException("Errore Db");
		}
	}
	
	public void dropTmpTable() {
		final String sql = "drop table if exists eprintids;";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			int rs = st.executeUpdate();
			conn.close();
		}catch(SQLException e) {
			 e.printStackTrace();
				throw new RuntimeException("Errore Db");
		}
	}
	
	public List<Author> getCoauthors(Author a)
	{
		List<Author> ltemp = new LinkedList<Author>();
		
		this.dropTmpTable();
		this.createTmpTable();
		this.InsertIntoTmpTable(a);
	
		final String sql = "select distinct authorid from creator inner join eprintids on eprintids.eprintid = creator.eprintid where creator.authorid != ?;" ;
	
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, a.getId());
			ResultSet rs = st.executeQuery();
			
			
	      while(rs.next()) {
	    	  ltemp.add(new Author(rs.getInt("authorid"), "", ""));
	      }
	      conn.close();
	      
	}
		catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	
		return ltemp;
		
	}
	
	public String getArticoloComune(Author a1, Author a2) {
		this.dropTmpTable();
		this.createTmpTable();
		this.InsertIntoTmpTable(a1);
		final String sql = "select paper.title from paper where paper.eprintid=(select creator.eprintid from creator inner join eprintids on eprintids.eprintid=creator.eprintid where creator.authorid=? limit 1); ";
	   try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, a2.getId());
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				return rs.getString("title");
			}
			conn.close();
			
	   }
	   catch (SQLException e) {
		   e.printStackTrace();
	   }
	   
	   return "";
	}
	
	
	
}