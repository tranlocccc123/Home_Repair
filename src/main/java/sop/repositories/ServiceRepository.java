package sop.repositories;

import java.util.List;
import sop.modelviews.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sop.models.Services;

@Repository
public class ServiceRepository {
	@Autowired
	JdbcTemplate db;

	public List<Services> findAll() {
		String sql = String.format("SELECT * FROM tbl_services");
		return db.query(sql, new Service_mapper());
	}

	public void addService(Services service) {
		String sql = "INSERT INTO tbl_services (ServiceName, Description, CreatedAt, Price, Status) VALUES (?, ?, ?, ?, ?)";
		db.update(sql, service.getServiceName(), service.getDescription(), service.getCreatedAt(), service.getPrice(),
				service.getStatus());
	}

	public Services findImageByServiceId(int id) {
		String str_query = String.format("select * from tbl_services where ServiceID=? ");
		return db.queryForObject(str_query, new Service_mapper(), new Object[] { id });
	}

    public int getTotalServices() {
        String sql = "SELECT COUNT(*) FROM tbl_services";
        return db.queryForObject(sql, Integer.class);
    }
    public List<Services> findAllPaginated(int page, int size) {
        String sql = "SELECT * FROM tbl_services ORDER BY ServiceID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        int offset = page * size;
        return db.query(sql, new Service_mapper(), new Object[]{offset, size}); // offset comes before size
    }

}
