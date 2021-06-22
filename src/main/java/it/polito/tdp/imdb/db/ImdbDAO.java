package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Archi;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public void listAllActors(Map<Integer,Actor> idMap){
		String sql = "SELECT * FROM actors";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("id"))) {
					Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
					idMap.put(actor.getId(), actor);
				}
			}
			conn.close();
	
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> listAllGenres(){
		String sql="SELECT DISTINCT m.genre as gen FROM movies_genres m ORDER BY m.genre asc";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("gen"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Actor> getVertici(Map<Integer,Actor> idMap, String genere){
		String sql="SELECT distinct r.actor_id as id FROM roles r, movies_genres m WHERE m.movie_id=r.movie_id && m.genre=?";
		List<Actor> result=new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(idMap.containsKey(res.getInt("id"))) {
					result.add(idMap.get(res.getInt("id")));
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Archi> getArchi(Map<Integer,Actor> idMap,String genere){
		String sql="SELECT r1.actor_id AS id1, r2.actor_id AS id2, COUNT(m.movie_id) AS peso FROM roles r1, roles r2, movies_genres m "
				+ "WHERE r1.actor_id>r2.actor_id && m.movie_id=r1.movie_id && r1.movie_id=r2.movie_id && m.genre=? "
				+ "GROUP BY r1.actor_id, r2.actor_id";
		List<Archi> result=new ArrayList<Archi>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(idMap.containsKey(res.getInt("id1")) && idMap.containsKey(res.getInt("id2"))) {
					Archi a=new Archi(idMap.get(res.getInt("id1")), idMap.get(res.getInt("id2")), res.getDouble("peso"));
					result.add(a);
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
