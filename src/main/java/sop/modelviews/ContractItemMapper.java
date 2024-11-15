package sop.modelviews;

import org.springframework.jdbc.core.RowMapper;
import sop.models.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContractItemMapper implements RowMapper<ContracItems> {

    @Override
    public ContracItems mapRow(ResultSet rs, int rowNum) throws SQLException {
    	ContracItems contractItem = new ContracItems();
        
        contractItem.setContractItemID(rs.getInt("ContractItemID"));
        contractItem.setContractID(rs.getInt("ContractID"));
        contractItem.setQuoteItemID(rs.getInt("QuoteItemID"));
        contractItem.setItemDescription(rs.getString("ItemDescription"));
        contractItem.setStatus(rs.getString("Status"));
        contractItem.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());

        return contractItem;
    }
}
