package sop.modelviews;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sop.models.Users;

public class User_mapper implements RowMapper<Users> {
    @Override
    public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
        Users user = new Users();       
        user.setUserId(rs.getInt("userId"));
        user.setUsername(rs.getString("userName"));
        user.setPasswordHash(rs.getString("passwordHash"));
        user.setFullName(rs.getString("fullName"));
        user.setEmail(rs.getString("email"));
        user.setPhoneNumber(rs.getString("phoneNumber"));
        user.setStatus(rs.getInt("status"));
        user.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime()); // Convert SQL Timestamp to LocalDateTime
        user.setRole(rs.getInt("role"));
        user.setToken(rs.getString("token"));
        
        return user;
    }
}
