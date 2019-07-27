package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.LibraryBranch;

@Component
public class BranchDAO extends BaseDAO<LibraryBranch> implements ResultSetExtractor<List<LibraryBranch>> {

	public void addLibraryBranch(LibraryBranch branch) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("insert into tbl_library_branch (branchName) values (?)", new Object[] { branch.getBranchName() });
	}

	public void updateLibraryBranch(LibraryBranch branch) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("update tbl_library_branch set branchName = ? where branchId = ?",
				new Object[] { branch.getBranchName(), branch.getBranchId() });
	}

	public void deleteLibraryBranch(LibraryBranch branch) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("delete from tbl_library_branch where branchId = ?", new Object[] { branch.getBranchId() });
	}

	public List<LibraryBranch> readAllBranches() throws SQLException, ClassNotFoundException {
		return mysqlTemplate.query("select * from tbl_library_branch", this);
	}

	
	public List<LibraryBranch> extractData(ResultSet rs) throws SQLException {
		List<LibraryBranch> branches = new ArrayList<>();
		//BranchDAO bdao = new BranchDAO(conn);
		while (rs.next()) {
			LibraryBranch branch = new LibraryBranch();
			branch.setBranchId(rs.getInt("branchId"));
			branch.setBranchName(rs.getString("branchName"));
			branch.setBranchAddress(rs.getString("branchAddress"));
			// populate my books
			// LibraryBranch.setBooks(bdao.readFirstLevel("select * from tbl_book where
			// bookId IN ( select bookId from tbl_book_authors where authorId = ?)", new
			// Object[]{LibraryBranch.getLibraryBranchId()}));
			// LibraryBranchs.add(LibraryBranch);
			branches.add(branch);
		}
		return branches;
	}


}
//}
