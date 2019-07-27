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

import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;

/**
 * @author ppradhan
 *
 */

@Component
public class PublisherDAO extends BaseDAO<Publisher> implements ResultSetExtractor<List<Publisher>> {

	public void addPublisher(Publisher publisher) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("insert into tbl_publisher (publisherName) values (?)", new Object[] { publisher.getPublisherName() });
	}

	public void updatePublisher(Publisher publisher) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("update tbl_publisher set publisherName = ? where  publisherId= ?",
				new Object[] { publisher.getPublisherName(), publisher.getPublisherId() });
	}

	public void deletePublisher(Publisher publisher) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("delete from tbl_publisher where publisherId = ?", new Object[] { publisher.getPublisherId() });
	}

	public List<Publisher> readAllPublishers() throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_publisher", this);
	}
	
	public List<Publisher> readBooksPerPublisher(Publisher publisher) throws ClassNotFoundException, SQLException{
		return mysqlTemplate.query("select * from tbl_books where pubId =?", new Object[] {publisher.getPublisherId()}, this);
	}

	public List<Publisher> extractData(ResultSet rs) throws SQLException {
		List<Publisher> publishers = new ArrayList<>();
		//BookDAO bdao = new BookDAO(conn);
		while (rs.next()) {
			Publisher publisher = new Publisher();
			publisher.setPublisherId(rs.getInt("publisherId"));
			publisher.setPublisherName(rs.getString("publisherName"));
			publisher.setPublisherAddress(rs.getString("publisherAddress"));
			publisher.setPublisherPhone(rs.getString("publisherPhone"));
			// populate my books
			// borrowers.setBooks(bdao.readFirstLevel("select * from tbl_book where bookId
			// IN ( select bookId from tbl_book_authors where authorId = ?)", new
			// Object[]{author.getBorrowerId()}));
			publishers.add(publisher);
		}
		return publishers;
	}

}
