package sop.modelviews;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import sop.models.*;

public class Contract_mapper implements RowMapper<Contracts> {

    @Override
    public Contracts mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contracts contract = new Contracts();

        contract.setContractId(rs.getInt("ContractID"));
        contract.setContractCode(rs.getString("ContractCode"));
        contract.setUserId(rs.getInt("UserID"));
        contract.setQuoteId(rs.getInt("QuoteID"));
        contract.setContractDate(rs.getTimestamp("ContractDate"));
        contract.setSignDate(rs.getTimestamp("SignDate"));
        contract.setDeposit(rs.getDouble("Deposit"));
        contract.setPaymentStages(rs.getString("PaymentStages"));
        contract.setStatus(rs.getString("Status"));

        return contract;
    }
}
