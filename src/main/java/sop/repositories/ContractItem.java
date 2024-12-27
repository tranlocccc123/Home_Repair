package sop.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sop.models.*;
import sop.modelviews.*;

@Repository
public class ContractItem {
	@Autowired
	JdbcTemplate db;

	public List<Contracts> getContractId(String id) {
		String sql = "SELECT ContractID FROM tbl_contracts WHERE ContractCode = ?";
		return db.query(sql, new Contract_mapper(), id);
	}

	public List<Integer> getContractIdsByCode(String contractCode) {
		String sql = "SELECT ContractID FROM tbl_contracts WHERE ContractCode = ?";
		// Trả về danh sách các ContractID
		return db.queryForList(sql, Integer.class, contractCode);
	}

	public List<ContracItems> getItemsByContractCode(String contractCode) {
	    // Lấy danh sách các ContractID từ ContractCode
	    List<Integer> contractIds = getContractIdsByCode(contractCode);

	    // Danh sách để lưu tất cả các ContractItem
	    List<ContracItems> contractItems = new ArrayList<>();

	    // Lặp qua từng ContractID và truy vấn bảng tbl_contractItems
	    for (Integer contractId : contractIds) {
	        String sql = "SELECT * FROM tbl_contractItems WHERE ContractID = ?";
	        // Sử dụng đúng kiểu dữ liệu ContractItem
	        List<ContracItems> items = db.query(sql, new ContractItemMapper(), contractId);
	        contractItems.addAll(items);  // Thêm tất cả các mục hợp đồng vào danh sách contractItems
	    }

	    return contractItems;  // Trả về danh sách tất cả ContractItems
	}

}




