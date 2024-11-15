package sop.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sop.modelviews.*;
import sop.utils.SecurityUtility;
import sop.models.Users;

@Repository
public class UserRepository {
	@Autowired
	JdbcTemplate db;

	public Users findById(int userId) {
		String sql = "SELECT * FROM tbl_users WHERE UserID = ?";
		try {
			return db.queryForObject(sql, new User_mapper(), new Object[] { userId });
		} catch (Exception e) {
			return null; // User not found
		}
	}

	public Integer findIdByUsername(String username) {
		String sql = "SELECT UserID FROM tbl_users WHERE Username = ?";
		try {
			return db.queryForObject(sql, new Object[] { username }, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void updateUser(Users user) {
		try {
			String sql = "UPDATE tbl_users SET FullName = ?, Email = ?, PhoneNumber = ? WHERE UserID = ?";
			db.update(sql, user.getFullName(), user.getEmail(), user.getPhoneNumber(), user.getUserId());
		} catch (DuplicateKeyException e) {
			throw new IllegalArgumentException("Some information(username, email, phone) may already exists.");
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public Users findByUsername(String username, int id) {
		String sql = "SELECT * FROM users WHERE username = ? and userId <> ?";
		try {
			return db.queryForObject(sql, new User_mapper(), new Object[] { username, id });
		} catch (Exception e) {
			return null; 
		}
	}

	public String editProfile(Users user) {
	    try {
	        StringBuilder queryBuilder = new StringBuilder("UPDATE tbl_users SET ");
	        List<Object> params = new ArrayList<>();

	        // Build the query dynamically based on provided user information
	        if (user.getFullName() != null && !user.getFullName().isEmpty()) {
	            queryBuilder.append("FullName = ?, ");
	            params.add(user.getFullName());
	        }
	        if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
	            queryBuilder.append("PasswordHash = ?, ");
	            String hashPassword = SecurityUtility.encryptBcrypt(user.getPasswordHash());
	            params.add(hashPassword);
	        }
	        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
	            queryBuilder.append("Email = ?, ");
	            params.add(user.getEmail());
	        }
	        if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
	            queryBuilder.append("PhoneNumber = ?, ");
	            params.add(user.getPhoneNumber());
	        }

	        // If no fields to update, return a message
	        if (params.isEmpty()) {
	            return "No fields to update";
	        }

	        // Remove the trailing comma and space, and add the WHERE clause
	        queryBuilder.setLength(queryBuilder.length() - 2); // Remove the last ", "
	        queryBuilder.append(" WHERE UserID = ?");
	        params.add(user.getUserId());

	        // Execute the update and return result
	        int rowsAffected = db.update(queryBuilder.toString(), params.toArray());
	        return rowsAffected == 1 ? "success" : "failed";

	    } catch (DuplicateKeyException e) {
	        throw new IllegalArgumentException("Some information (username, email, phone) may already exist.");
	    } catch (Exception e) {
	        return "Error: " + e.getMessage();
	    }
	}
	public void saveVerificationToken(int userId, String token) {
        String sql = "UPDATE tbl_users SET Token = ? WHERE UserID = ?";
        db.update(sql, token, userId);
    }

    public Integer findUserIdByToken(String token) {
        String sql = "SELECT UserID FROM tbl_users WHERE Token = ?";
        return db.queryForObject(sql, new Object[]{token}, Integer.class);
    }

    public void verifyUser(int userId) {
        String sql = "UPDATE tbl_users SET Status = 1, Token = NULL WHERE UserID = ?";
        db.update(sql, userId);
    }
    public Integer findUserIdByEmail(String email) {
        String sql = "SELECT UserID FROM tbl_users WHERE Email = ?";
        return db.queryForObject(sql, new Object[]{email}, Integer.class);
    }
    public List<Users> findAll() {
		String sql = "SELECT * FROM tbl_users";
		return db.query(sql, new User_mapper());
	}
	

}
