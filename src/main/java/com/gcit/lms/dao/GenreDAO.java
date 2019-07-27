/**
 * 
 */
package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Genre;

/**
 * @author ppradhan
 *
 */
@Component
public class GenreDAO extends BaseDAO<Genre> implements ResultSetExtractor<List<Genre>>{
	

	public void addGenre(Genre genre) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("insert into tbl_genre (genreName) values (?)", new Object[]{genre.getGenreName()});
	}

	public void updateGenre(Genre genre) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("update tbl_genre set genreName = ? where genreId = ?", new Object[]{genre.getGenreName(), genre.getGenreId()});
	}

	public void deleteGenre(Genre genre) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("delete from tbl_genre where genreId = ?", new Object[]{genre.getGenreId()});
	}
	
	public List<Genre> readAllGenres() throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_genre", this);
	}
	
	public List<Genre> readAllGenresByName(String genreName) throws ClassNotFoundException, SQLException {
		genreName = "%"+genreName+"%";
		return mysqlTemplate.query("select * from tbl_genre where genreName like ?",new Object[]{genreName}, this);
	}

	public List<Genre> extractData(ResultSet rs) throws SQLException{
		List<Genre> genres = new ArrayList<>();
		while(rs.next()){
			Genre genre = new Genre();
			genre.setGenreId(rs.getInt("genre_id"));
			genre.setGenreName(rs.getString("genre_name"));
			genres.add(genre);
		}
		return genres;
	}
	
	
}
