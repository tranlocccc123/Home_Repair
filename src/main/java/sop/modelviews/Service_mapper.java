package sop.modelviews;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sop.models.Services;
import sop.models.Users;

public class Service_mapper implements RowMapper<Services> {
	 @Override
	    public Services mapRow(ResultSet rs, int rowNum) throws SQLException {
	        Services service = new Services();
	        service.setServiceId(rs.getInt("ServiceID"));
	        service.setServiceName(rs.getString("ServiceName"));
	        service.setDescription(rs.getString("Description"));
	        service.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
	        service.setPrice(rs.getBigDecimal("Price"));
	        service.setStatus(rs.getInt("Status"));
	        return service;
	    }
}
