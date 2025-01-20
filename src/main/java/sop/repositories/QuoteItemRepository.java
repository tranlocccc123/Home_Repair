package sop.repositories;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sop.models.QuoteItems;
import sop.models.Quotes;
import sop.modelviews.QuoteItem_mapper;
import sop.modelviews.Quotes_mapper;

@Repository
public class QuoteItemRepository {
    @Autowired
	JdbcTemplate db;

	public void addQuote(QuoteItems quoteitem) {
		String sql = "INSERT INTO tbl_quoteItems (QuoteID, ServiceID, ItemDescription, Quantity, UnitPrice, TotalPrice, CreatedAt, Notes) "
				+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";

		db.update(sql, quoteitem.getQuoteId(), quoteitem.getServiceId(), quoteitem.getItemDescription(),
                        quoteitem.getQuantity(), quoteitem.getUnitPrice(), quoteitem.getTotalPrice(),
                        quoteitem.getCreatedAt(), quoteitem.getNotes());
	}

	@SuppressWarnings("deprecation")
    public List<QuoteItems> getQuoteItem(int quoteid) {
		String sql = "select * from tbl_quoteItems where QuoteID=?";
		return db.query(sql, new Object[]{quoteid} ,new QuoteItem_mapper());
	}
	public QuoteItems getItemQuobyID(int id) {
		String sql="select * from tbl_quoteItems where QuoteID=?";
		db.query(sql,new QuoteItem_mapper(),new Object[] {id});
		return null;
	}
	
    public List<QuoteItems> getAllQuoteItem() {
        String sql = "SELECT * FROM tbl_quoteItems";
		return db.query(sql, new QuoteItem_mapper());
	}

    public void saveQuoteItem(QuoteItems quoteItem)
    {
        Integer quoteItemId = quoteItem.getQuoteItemId();
        if (quoteItemId != null) { 
        // String sql = "INSERT INTO tbl_quoteItems (QuoteID, ServiceID, ItemDescription, Quantity, UnitPrice, CreatedAt, Notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sql = "UPDATE tbl_quoteItems SET QuoteID = ?, ServiceID = ?, ItemDescription = ?, Quantity = ?, UnitPrice = ?, CreatedAt = ?, Notes = ? WHERE QuoteItemID = ?";

         db.update(sql,
         quoteItem.getQuoteId(),
         quoteItem.getServiceId(),
         quoteItem.getItemDescription(),
          quoteItem.getQuantity(),
          quoteItem.getUnitPrice(),
          quoteItem.getCreatedAt(),
          quoteItem.getNotes(),
          quoteItem.getQuoteItemId());

 
        }
        else {
            throw new IllegalArgumentException("QuoteID " + quoteItemId + " không tồn tại trong bảng tbl_quotes.");
        }
    }
    private boolean quoteExists(Integer quoteId) {
        String checkSql = "SELECT COUNT(*) FROM tbl_quotes WHERE QuoteID = ?";
        Integer count = db.queryForObject(checkSql, Integer.class, quoteId);
        return count != null && count > 0;
    }
    public QuoteItems getQuoteById(int quoteid) {
 		String str_query = String.format("select * from tbl_quoteItems where QuoteID=?");
 		return db.queryForObject(str_query, new QuoteItem_mapper(), new Object[] { quoteid });

 	}
     public QuoteItems getQuoteItemByQuoteItemID(int quoteItemId) {
		//String sql="select * from tbl_quoteItems where QuoteItemID=?";
		String str_query = String.format("select * from tbl_quoteItems where QuoteItemID=?");
        return db.queryForObject(str_query, new QuoteItem_mapper(), new Object[] { quoteItemId });
	}
	
}

