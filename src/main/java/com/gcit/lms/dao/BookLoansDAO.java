package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.BookLoans;

@Component
public class BookLoansDAO extends BaseDAO<BookLoans> implements ResultSetExtractor<List<BookLoans>> {


	public void addBookLoan(BookLoans bookLoan) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("insert into tbl_book_loans (bookId,branchId,cardNo,dateOut,dueDate) values (?,?,?,now(),now()+ interval 7 day)",
				new Object[] { bookLoan.getBookId(), bookLoan.getBranchId(), bookLoan.getBorrowerId() });
		updateSubtractCopies(bookLoan);
		// INSERT INTO `library`.`tbl_book_loans` (`bookId`, `branchId`, `cardNo`,
		// `dateOut`,`dueDate`)
		// VALUES ('8', '8', '9', curdate(), curdate()+ interval 7 day );
	}

	public Integer addBookGetPK(Book book) throws ClassNotFoundException, SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		mysqlTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement("insert into tbl_book (title) values (?)");
			ps.setString(1, book.getTitle());
			return ps;
		}, keyHolder);
		return (Integer) keyHolder.getKey();
	}
	
	// TO BE MOVED TO BOOK COPIES
	public void updateAddCopies(BookLoans bookloan) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("update tbl_book_copies set noOfCopies = noOfCopies+1 where branchId =? and bookId = ?",
				new Object[] { bookloan.getBranchId(), bookloan.getBookId() });
	}

	// TO BE MOVED TO BOOK COPIES
	public void updateSubtractCopies(BookLoans bookloan) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("update tbl_book_copies set noOfCopies = noOfCopies-1 where branchId =? and bookId = ?",
				new Object[] { bookloan.getBranchId(), bookloan.getBookId() });
	}

	public void updateBookLoan(BookLoans bookLoan) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("update tbl_book_loans set dateIn = now() where bookId = ? and cardNo = ? and branchId = ?",
				new Object[] { bookLoan.getBookId(), bookLoan.getBorrowerId(), bookLoan.getBranchId() });
	}

	public void deleteBookLoan(BookLoans bookloan) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("delete from tbl_book_loans where bookId = ? and branchId =? and cardNo = ?",
				new Object[] { bookloan.getBookId(), bookloan.getBranchId(), bookloan.getBorrowerId() });
		updateAddCopies(bookloan);
	}

	public List<BookLoans> userLoanList(BookLoans bookLoan) throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_book_loans where cardNo = ? and dateIn is null",
				new Object[] { bookLoan.getBorrowerId() }, this);
	}

	public List<BookLoans> readBorrowedBooks(BookLoans bookLoan) throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_book_loans where cardNo =? and branchId = ? and dateIn is null",
				new Object[] { bookLoan.getBorrowerId(), bookLoan.getBranchId() }, this);
	}

	public List<BookLoans> readAllBookLoans() throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_book_loans", this);
	}

	public List<BookLoans> extractData(ResultSet rs) throws SQLException {
		List<BookLoans> bookLoans = new ArrayList<>();
		while (rs.next()) {
			BookLoans bookLoan = new BookLoans();
			bookLoan.setBookId(rs.getInt("bookId"));
			bookLoan.setBorrowerId(rs.getInt("cardNo"));
			bookLoan.setBranchId(rs.getInt("branchId"));
			bookLoan.setDateDue(rs.getDate("dueDate"));
			// populate all child entities (genres, authors, branches etc)
			// book.setAuthors(adao.read("select * from tbl_author where authorId IN (
			// select authorId from tbl_book_authors where bookId = ?)", new Object[]
			// {book.getBookId()}));
			bookLoans.add(bookLoan);
		}
		return bookLoans;
	}

	public void overrideLoan(BookLoans bookLoan) throws ClassNotFoundException, SQLException {

		mysqlTemplate.update("update tbl_book_loans set dueDate = dueDate + interval 7 day where cardNo =? and bookId = ? and branchId=?",
				new Object[] { bookLoan.getBorrowerId(), bookLoan.getBookId(), bookLoan.getBranchId() });
	}

}
