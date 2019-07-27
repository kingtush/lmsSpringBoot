package com.gcit.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoansDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.BookLoans;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.LibraryBranch;
import com.gcit.lms.entity.Publisher;

@RestController
public class AdminService extends BaseService {

	@Autowired
	BookDAO bdao;

	@Autowired
	AuthorDAO adao;
	
	@Autowired
	BranchDAO brdao;
	
	@Autowired
	GenreDAO gdao;
	
	@Autowired
	PublisherDAO pdao;
	
	@Autowired
	BookCopiesDAO bcdao;
	
	@Autowired
	BorrowerDAO borrowdao;
	
	@Autowired
	BookLoansDAO bldao;
	
	

	@Transactional
	// @Qualifier
	@RequestMapping(value= "/addBook", method=RequestMethod.POST, consumes="application/json")
	public String addBook(@RequestBody Book book) {
		try {
			Integer bookId = bdao.addBookGetPK(book);
			// add authors
			for (Author a : book.getAuthors()) {
				bdao.addBookAuthors(bookId, a.getAuthorId());
			}
			// add genres
			for(Genre a : book.getGenres()) {
				bdao.addBookGenres(bookId, a.getGenreId());
			}
			
			if(book.getPublisher()!=null) {
			bdao.addPublisher(book.getPublisher().getPublisherId(),bookId);
			}
			
			// add branches
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Something went wrong. Failed to get all authors");
		}
		return null;
	}
	
	@RequestMapping(value= "/readAllAuthors/{authorName}", method=RequestMethod.GET, produces="application/json")
	public List<Author> readAuthors(@PathVariable String authorName) {
		List<Author> authors = new ArrayList<>();
		try {
			authors = adao.readAllAuthorsByName(authorName);
			for(Author a: authors){
				a.setBooks(bdao.readAllBooksByAuthorId(a.getAuthorId()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Something went wrong. Failed to get all authors");
		}
		return authors;
	}
	
	@RequestMapping(value= "/readAllAuthors/", method=RequestMethod.GET, produces="application/json")
	public List<Author> readAllAuthors() throws ClassNotFoundException, SQLException {
		List<Author> authors = new ArrayList<>();
		authors = adao.readAllAuthors();
		try {
			
			for(Author a: authors){
				a.setBooks(bdao.readAllBooksByAuthorId(a.getAuthorId()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Something went wrong. Failed to get all authors");
		}
		return authors;
	}
	
	@RequestMapping(value= "/readAuthors", method=RequestMethod.GET, produces="application/json")
	public List<Author> readAuthorsByName(@RequestParam(value="authorName") String authorName) {
		List<Author> authors = new ArrayList<>();
		try {
			if(authorName!=null || authorName.length() > 0){
				authors = adao.readAllAuthorsByName(authorName);
			}else{
				authors = adao.readAllAuthors();
			}
			for(Author a: authors){
				a.setBooks(bdao.readAllBooksByAuthorId(a.getAuthorId()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Something went wrong. Failed to get all authors");
		}
		return authors;
	}
	
	@RequestMapping(value= "/saveAuthor", method=RequestMethod.POST, consumes="application/json")
	public String saveAuthor(@RequestBody Author author) {
		try{
			if(author.getAuthorId()!=null && author.getAuthorName() !=null){
				adao.updateAuthor(author);
			}else if(author.getAuthorId()!=null){
				adao.deleteAuthor(author);
			}else{
				adao.addAuthor(author);
				//add into tbl_book_authors
			}
			return "Author added sucessfully";
		}catch (Exception e){
			e.printStackTrace();
			return "Author add failed";
		}
	}
	
	//READ BORROWERS
	@RequestMapping(value= "/readBorrowers", method=RequestMethod.GET, consumes="application/json")
	public List<Borrower> readAllBorrowers() throws SQLException {
		
		List<Borrower> borrowers = new ArrayList<>();
		System.out.println("TRYING TO READ BORROWERS");
		try {

			// System.out.println("made it here");
			borrowers = borrowdao.readAllBorrowers();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Something went wrong. Failed to get all borrowers");
		} finally {

			 System.out.println("finally");
			 
		}
		return borrowers;
	}

	
	//READ PUBLISHERS
	@RequestMapping(value= "/readPublishers", method=RequestMethod.GET, consumes="application/json")
public List<Publisher> readAllPublishers() throws SQLException {
		
		List<Publisher> publishers = new ArrayList<>();
		System.out.println("TRYING TO READ BORROWERS");
		try {

			// System.out.println("made it here");
			publishers = pdao.readAllPublishers();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Something went wrong. Failed to get all publishers");
		} finally {

			 System.out.println("finally");
			 
		}
		return publishers;
	}

	//READ BRANCHES
	@RequestMapping(value= "/readBranches", method=RequestMethod.GET, consumes="application/json")
	public List<LibraryBranch> readAllLibraryBranches() throws SQLException {
			
			List<LibraryBranch> branches = new ArrayList<>();
			System.out.println("TRYING TO READ BORROWERS");
			try {

				// System.out.println("made it here");
				branches = brdao.readAllBranches();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("Something went wrong. Failed to get all branches");
			} finally {

				 System.out.println("finally");
				 
			}
			return branches;
		}
	
	
	//READING THE GENRES
	@RequestMapping(value= "/readGenres", method=RequestMethod.GET, consumes="application/json")
	public List<Genre> readAllGenres() throws SQLException {
			
			List<Genre> genres = new ArrayList<>();
			System.out.println("TRYING TO READ Genres");
			try {

				// System.out.println("made it here");
				genres = gdao.readAllGenres();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("Something went wrong. Failed to get all genres");
			} finally {

				 System.out.println("finally");
				 
			}
			return genres;
		}
	
	@RequestMapping(value= "/readBookLoans", method=RequestMethod.GET, consumes="application/json")
	public List<BookLoans> readAllBookLoans() throws SQLException {
			
			List<BookLoans> bookloans = new ArrayList<>();
			System.out.println("TRYING TO READ BookLoans");
			try {

				// System.out.println("made it here");
				bookloans = bldao.readAllBookLoans();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("Something went wrong. Failed to get all bookloans");
			} finally {

				 System.out.println("finally");
				 
			}
			return bookloans;
		}
	
	@RequestMapping(value= "/readBookCopies", method=RequestMethod.GET, consumes="application/json")
	public List<BookCopies> readAllBookCopies() throws SQLException {
			
			List<BookCopies> bookcopies = new ArrayList<>();
			System.out.println("TRYING TO READ BookCopies");
			try {

				// System.out.println("made it here");
				bookcopies = bcdao.readAllBookCopies();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("Something went wrong. Failed to get all bookcopies");
			} finally {

				 System.out.println("finally");
				 
			}
			return bookcopies;
		}
	
	@RequestMapping(value= "/saveBorrower", method=RequestMethod.POST, consumes="application/json")
	public String saveBorrower(@RequestBody Borrower borrower) {
		try{
			if(borrower.getBorrowerId()!=null && borrower.getBorrowerName() !=null){
				borrowdao.updateBorrower(borrower);
			}else if(borrower.getBorrowerId()!=null){
				borrowdao.deleteBorrower(borrower);
			}else{
				borrowdao.addBorrower(borrower);
				//add into tbl_book_borrowers
			}
			return "Borrower modified sucessfully";
		}catch (Exception e){
			e.printStackTrace();
			return "Borrower add failed";
		}
	}

	@RequestMapping(value= "/savePublisher", method=RequestMethod.POST, consumes="application/json")
	public String savePublisher(@RequestBody Publisher publisher) {
		try{
			if(publisher.getPublisherId()!=null && publisher.getPublisherName() !=null){
				pdao.updatePublisher(publisher);
			}else if(publisher.getPublisherId()!=null){
				pdao.deletePublisher(publisher);
			}else{
				pdao.addPublisher(publisher);
				//add into tbl_book_publishers
			}
			return "Publisher modified sucessfully";
		}catch (Exception e){
			e.printStackTrace();
			return "Publisher modification failed";
		}
	}
	
	@RequestMapping(value= "/saveBranch", method=RequestMethod.POST, consumes="application/json")
	public String saveBranch(@RequestBody LibraryBranch branch) {
		try{
			if(branch.getBranchId()!=null && branch.getBranchName() !=null){
				brdao.updateLibraryBranch(branch);
			}else if(branch.getBranchId()!=null){
				brdao.deleteLibraryBranch(branch);
			}else{
				brdao.addLibraryBranch(branch);
				//add into tbl_book_publishers
			}
			return "Branch modified sucessfully";
		}catch (Exception e){
			e.printStackTrace();
			return "Branch modification failed";
		}
	}
	
	@RequestMapping(value= "/saveBookCopies", method=RequestMethod.POST, consumes="application/json")
	public String saveBookCopies(@RequestBody BookCopies bookCopies) {
		try{
			if(bookCopies.getBranchId()!=null && bookCopies.getBookId() !=null && bookCopies.getNoOfCopies()!=null){
				bcdao.updateBookCopies(bookCopies);
			}else if(bookCopies.getBranchId()!=null){
				bcdao.deleteBookCopies(bookCopies);
			}else{
				bcdao.addBookCopies(bookCopies);
				//add into tbl_book_publishers
			}
			return "Branch modified sucessfully";
		}catch (Exception e){
			e.printStackTrace();
			return "Branch modification failed";
		}
	}
	
	@RequestMapping(value= "/saveBookLoans", method=RequestMethod.POST, consumes="application/json")
	public String saveBookLoans(@RequestBody BookLoans bookLoans) {
		try{
			if(bookLoans.getBranchId()!=null && bookLoans.getBookId() !=null) {
				bldao.overrideLoan(bookLoans);
			}else if(bookLoans.getBranchId()!=null){
				bldao.deleteBookLoan(bookLoans);
			}else{
				bldao.addBookLoan(bookLoans);
				//add into tbl_book_publishers
			}
			return "Branch modified sucessfully";
		}catch (Exception e){
			e.printStackTrace();
			return "Branch modification failed";
		}
	}
	
	@RequestMapping(value= "/saveGenre", method=RequestMethod.POST, consumes="application/json")
	public String saveGenre(@RequestBody Genre genres) {
		try{
			if(genres.getGenreId()!=null && genres.getGenreName() !=null) {
				gdao.updateGenre(genres);
			}else if(genres.getGenreId()!=null){
				gdao.deleteGenre(genres);
			}else{
				gdao.addGenre(genres);
				//add into tbl_book_publishers
			}
			return "Branch modified sucessfully";
		}catch (Exception e){
			e.printStackTrace();
			return "Branch modification failed";
		}
	}
	
	//Publisher-read,update/add/delete
	//Genre-read,update/add/delete
	//Borrower-read,update/add/delete
	//AUTHOR-read, update/add/delete
	//Branch-read,update/add/delete
	//Bookloans-read,update(override)/add/delete
	//Bookcopies-read,update/add/delete
	//Book-read,add
}
