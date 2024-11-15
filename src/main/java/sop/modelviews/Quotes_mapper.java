package sop.modelviews;

import org.springframework.jdbc.core.RowMapper;
import sop.models.Quotes;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Quotes_mapper implements RowMapper<Quotes> {

    @Override
    public Quotes mapRow(ResultSet rs, int rowNum) throws SQLException {
        Quotes quotes = new Quotes();
        
        quotes.setQuoteId(rs.getInt("QuoteID"));
        quotes.setEmployeeId(rs.getInt("EmployeeID"));
        quotes.setCustomerId(rs.getInt("CustomerID"));
        quotes.setQuoteDate(rs.getTimestamp("QuoteDate").toLocalDateTime());
        quotes.setTotalAmount(rs.getBigDecimal("TotalAmount"));
        quotes.setStatus(rs.getString("status"));

        return quotes;
    }
}
