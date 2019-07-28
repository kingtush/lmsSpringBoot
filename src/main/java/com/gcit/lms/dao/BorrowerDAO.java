package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Borrower;

@Component
public class BorrowerDAO extends BaseDAO<Borrower> implements ResultSetExtractor<List<Borrower>>{


	public void addBorrower(Borrower borrower) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("insert into tbl_borrower (name) values (?)", new Object[] { borrower.getBorrowerName() });
	}

	public void updateBorrower(Borrower borrower) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("update tbl_borrower set name = ? where  cardNo= ?",
				new Object[] { borrower.getBorrowerName(), borrower.getBorrowerId() });
	}

	public void deleteBorrower(Borrower borrower) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("delete from tbl_borrower where cardNo = ?", new Object[] { borrower.getBorrowerId() });
	}

	public List<Borrower> readAllBorrowers() throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_borrower", this);
	}


	public List<Borrower> extractData(ResultSet rs) throws SQLException {
		List<Borrower> borrowers = new ArrayList<>();
		//BookDAO bdao = new BookDAO(conn);
		while (rs.next()) {
			Borrower borrower = new Borrower();
			borrower.setBorrowerId(rs.getInt("cardNo"));
			borrower.setBorrowerName(rs.getString("name"));
			borrower.setBorrowerAddress(rs.getString("address"));
			// populate my books
			// borrowers.setBooks(bdao.readFirstLevel("select * from tbl_book where bookId
			// IN ( select bookId from tbl_book_authors where authorId = ?)", new
			// Object[]{author.getBorrowerId()}));
			borrowers.add(borrower);
		}
		return borrowers;
	}

}
