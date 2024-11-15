package sop.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sop.models.*;
import sop.modelviews.*;

@Repository
public class ContractRepository {
	@Autowired
	JdbcTemplate db;
	
	public List<Contracts> findByIdUser(int id) {
		String sql = "SELECT * FROM tbl_contracts where UserID =?";
		 return db.query(sql, new Contract_mapper(), new Object[]{id});
	}
	public ContracItems findbyContractID(int id) {
		String str_query = String.format("select * from tbl_contractItems where ContractID=? ");
		return db.queryForObject(str_query, new ContractItemMapper(), new Object[] { id });
	}
	public void saveContract(Contracts contract) {
		String sql = "INSERT INTO tbl_contracts (ContractCode, QuoteID, UserID, ContractDate, SignDate, Deposit, PaymentStages, Status) "
				+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";

		db.update(sql, contract.getContractCode(), contract.getQuoteId(), contract.getUserId(),
        contract.getContractDate(), contract.getSignDate(), contract.getDeposit(),
        contract.getPaymentStages(), contract.getStatus());
	}

	public void updateContract(Contracts contract) {
		String sql = "UPDATE tbl_contracts SET ContractCode =?, QuoteID=?, UserID=?, ContractDate=?, SignDate=?, Deposit=?, PaymentStages=?, Status=?";

		db.update(sql, contract.getContractCode(), contract.getQuoteId(), contract.getUserId(),
        contract.getContractDate(), contract.getSignDate(), contract.getDeposit(),
        contract.getPaymentStages(), contract.getStatus());
	}

	@SuppressWarnings("deprecation")
    public List<Contracts> getContracts(int userId) {
		String sql = "select * from tbl_contracts where UserID=?";
		return db.query(sql, new Object[]{userId} ,new Contract_mapper());
	}
}
