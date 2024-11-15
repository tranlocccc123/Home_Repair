package sop.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sop.models.Images;
import sop.models.Users;
import sop.modelviews.*;

@Repository
public class EmployeeRepository {
	@Autowired
	JdbcTemplate db;

	public void addUser(Users user) {
		String sql = "INSERT INTO tbl_users ( Username, PasswordHash, FullName, Email, PhoneNumber, Status, CreatedAt, Role, Token) "
				+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		db.update(sql, user.getUsername(), user.getPasswordHash(), user.getFullName(), user.getEmail(),
				user.getPhoneNumber(), user.getStatus(), user.getCreatedAt(), user.getRole(), user.getToken());
	}

	public List<Users> findAll() {
		String sql = "SELECT * FROM tbl_users where Role =1";
		return db.query(sql, new User_mapper());
	}
	public List<Users> findEmp() {
		String sql = "SELECT * FROM tbl_users where Role =1";
		return db.query(sql, new User_mapper());
	}
	
	
}
