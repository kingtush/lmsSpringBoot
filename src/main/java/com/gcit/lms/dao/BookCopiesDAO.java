package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.Genre;

@Component
public class BookCopiesDAO extends BaseDAO<BookCopies> implements ResultSetExtractor<List<BookCopies>> {


	public void updateBookCopies(BookCopies bookCopies) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("update tbl_book_copies set noOfCopies = ? where bookId = ? and branchId = ?",
				new Object[] { bookCopies.getNoOfCopies(), bookCopies.getBookId(), bookCopies.getBranchId() });
	}
	
	public void addBookCopies(BookCopies bookcopies) {
		mysqlTemplate.update("insert into tbl_book_copies (bookId,branchId,noOfCopies) values(?,?,?)", new Object[] {bookcopies.getBookId(),bookcopies.getBranchId(),bookcopies.getNoOfCopies()});
	}

	public void deleteBookCopies(BookCopies bookCopies) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("delete from tbl_book_copies where branchId = ?", new Object[] { bookCopies.getBookId() });
	}

	public List<BookCopies> readAllBookCopies() throws SQLException, ClassNotFoundException {
		return mysqlTemplate.query("select * from tbl_book_copies", this);
	}

	public List<BookCopies> readAvailableBooksForBranch(BookCopies bookCopies)
			throws SQLException, ClassNotFoundException {
		return mysqlTemplate.query("select * from tbl_book_copies where branchId=? and noOfCopies>0",
				new Object[] { bookCopies.getBranchId() },this);
	}

	public List<BookCopies> readAllBooksForBranch(BookCopies bookCopies) throws SQLException, ClassNotFoundException {
		return mysqlTemplate.query("select * from tbl_book_copies where branchId=?", new Object[] { bookCopies.getBranchId() }, this);
	}

	
	public List<BookCopies> numberOfCopies(BookCopies bookcopies) {
	return mysqlTemplate.query("select noOfCopies from tbl_book_copies where bookId = ? and branchId = ?", new Object []{bookcopies.getBookId(),bookcopies.getBranchId()}, this);
	}

//	public Book getBookName(BookCopies bookcopies) throws SQLException, ClassNotFoundException{
//		return read("select title from tbl_book where bookId =?", new Object[] {bookcopies.getBookId()});
//	}

	public List<BookCopies> extractData(ResultSet rs) throws SQLException {
		List<BookCopies> bookcopiesList = new ArrayList<>();
		// GenreDAO bdao = new GenreDAO(conn);
		while (rs.next()) {
			BookCopies bookcopies = new BookCopies();
			bookcopies.setBookId(rs.getInt("bookId"));
			bookcopies.setNoOfCopies(rs.getInt("noOfCopies"));
			// populate my books
			// LibraryBranch.setBooks(bdao.readFirstLevel("select * from tbl_book where
			// bookId IN ( select bookId from tbl_book_authors where authorId = ?)", new
			// Object[]{LibraryBranch.getLibraryBranchId()}));
			// LibraryBranchs.add(LibraryBranch);
			bookcopiesList.add(bookcopies);
		}
		return bookcopiesList;
	}


}
