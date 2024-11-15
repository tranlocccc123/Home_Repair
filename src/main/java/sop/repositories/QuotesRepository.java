package sop.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sop.models.*;
import sop.models.Services;
import sop.modelviews.*;

@Repository
public class QuotesRepository {
@Autowired
	JdbcTemplate db;

	public void addQuote(Quotes quote) {
		String sql = "INSERT INTO tbl_quotes (  EmployeeID, CustomerID, TotalAmount, QuoteDate, Status) "
				+ "VALUES ( ?, ?, ?, ?, ?)";

		db.update(sql, quote.getEmployeeId(), quote.getCustomerId(),
				quote.getTotalAmount(), quote.getQuoteDate(), quote.getStatus());
	}
	
	public List<Quotes> findPending(){
		   String sql = "SELECT * FROM tbl_quotes where EmployeeID = 0 ";
		return db.query(sql, new Quotes_mapper());
	}
	public List<Quotes> findAll() {
        String sql = "SELECT * FROM tbl_quotes";
        return db.query(sql, new Quotes_mapper());
    }

    // Find a quote by ID
    public Quotes findById(int id) {
        String sql = "SELECT * FROM tbl_quotes WHERE QuoteID = ?";
        return db.queryForObject(sql, new Quotes_mapper(), id);
    }

    // Save a new quote
    public int save(Quotes quote) {
        String sql = "INSERT INTO tbl_quotes (EmployeeID, CustomerID, QuoteDate, TotalAmount, Status) VALUES (?, ?, ?, ?, ?)";
        return db.update(sql, quote.getEmployeeId(), quote.getCustomerId(), quote.getQuoteDate(), quote.getTotalAmount(), quote.getStatus());
    }

    // Update an existing quote
    public int update(Quotes quote) {
        String sql = "UPDATE tbl_quotes SET EmployeeID = ?, Status = ? WHERE QuoteID = ?";
        return db.update(sql, quote.getEmployeeId(), quote.getStatus(), quote.getQuoteId());
    }

    // Delete a quote by ID
    public int deleteById(int id) {
        String sql = "DELETE FROM tbl_quotes WHERE QuoteID = ?";
        return db.update(sql, id);
    }
    
}
