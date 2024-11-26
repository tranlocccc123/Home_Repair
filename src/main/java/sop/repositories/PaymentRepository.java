package sop.repositories;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sop.models.*;
import sop.modelviews.*;

@Repository
public class PaymentRepository {

    @Autowired
	JdbcTemplate db;

	public void addPayment(Payment pay) {
		String sql = "INSERT INTO tbl_payment (NamePayment, ContractID, AmountMoney, CreatedAt, PaymentStage, StageDescription, Status) "
				+ "VALUES ( ?, ?, ?, ?, ?, ?, ?)";

		db.update(sql, pay.getNamePayment(), pay.getContractId(), pay.getAmountMoney(),pay.getCreatedAt(),
                pay.getPaymentStage(), pay.getStageDescription(), pay.getStatus());
	}

    @SuppressWarnings("deprecation")
    public List<Payment> getPaymentById(int paymentId) {
		String sql = "select * from tbl_contracts where contractId=?";
		return db.query(sql, new Object[]{paymentId} ,new Payment_Mapper());
	}

}
