package sop.modelviews;

import sop.models.Payment;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import sop.models.*;

public class Payment_Mapper  implements RowMapper<Payment> {

    @Override
    public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Payment payment = new Payment();

        payment.setContractId(rs.getInt("ContractID"));
        payment.setNamePayment(rs.getString("NamePayment"));
        payment.setPaymentStage(rs.getString("PaymentStage"));
        payment.setStageDescription(rs.getString("StageDescription"));
        payment.setCreatedAt(rs.getTimestamp("CreatedAt"));
        payment.setAmountMoney(rs.getBigDecimal("AmountMoney"));
        payment.setStatus(rs.getString("Status"));

        return payment;
    }
}
