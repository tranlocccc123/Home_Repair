package sop.modelviews;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sop.models.QuoteItems;

public class QuoteItem_mapper implements RowMapper<QuoteItems> {
    @Override
       public QuoteItems mapRow(ResultSet rs, int rowNum) throws SQLException {
        QuoteItems quotes = new QuoteItems();
            quotes.setQuoteItemId(rs.getInt("QuoteItemId"));
            quotes.setQuoteId(rs.getInt("QuoteID"));
            quotes.setServiceId(rs.getInt("ServiceID"));
            quotes.setItemDescription(rs.getString("ItemDescription"));
            quotes.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
            quotes.setQuantity(rs.getInt("Quantity"));
            quotes.setUnitPrice(rs.getBigDecimal("UnitPrice"));
            quotes.setTotalPrice(rs.getBigDecimal("TotalPrice"));
            quotes.setNotes(rs.getString("Notes"));
           return quotes;
       }
}
