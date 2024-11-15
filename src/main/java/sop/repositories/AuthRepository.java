package sop.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sop.modelviews.*;
import sop.models.Users;

@Repository
public class AuthRepository {
	@Autowired
	JdbcTemplate db;
	  public AuthRepository(JdbcTemplate db) {
	        this.db = db;
	    }
	
	public String findPasswordByUsername(String username) {
        String sql = "SELECT PasswordHash FROM tbl_users WHERE Username = ?";
        try {
            return db.queryForObject(sql, new Object[]{username}, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null; // Username not found
        }
    }

    public Integer findUserTypeByUsername(String username) {
        String sql = "SELECT Role FROM tbl_users WHERE Username = ?";
        try {
            return db.queryForObject(sql, new Object[]{username}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return null; // Username not found
        }
    }
    public Users findByUsername(String username) {
        String sql = "SELECT * FROM tbl_users WHERE Username = ?";
        
        return db.queryForObject(sql, new User_mapper(), new Object[]{username});
    }
}
