package sop.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sop.modelviews.*;
import sop.utils.FileUtility;
import sop.models.Images;

@Repository
public class ImageRepository {
	@Autowired
	JdbcTemplate db;

	public List<Images> findAll() {
		String sql = "SELECT * FROM tbl_images";
		return db.query(sql, new Image_mapper());
	}

	@SuppressWarnings("deprecation")
	public List<Images> findByServiceId(int ServiceId) {
		String sql = String.format("SELECT * FROM tbl_images WHERE ServiceID = ?");
		return db.query(sql, new Object[] { ServiceId }, new Image_mapper());
	}

	public void addImage(Images image) {
		String sql = "INSERT INTO tbl_images (ImageName, MainStatus, ServiceID, UserID, PostID, BannerID, ContractItemID, Status) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		db.update(sql, image.getImageName(), image.getMainStatus(), image.getServiceID(), image.getUserID(),
				image.getPostID(), image.getBannerID(), image.getContractItemID(), image.getStatus());
	}

	public void updateMainStatus(int serviceId, int mainStatus) {
		String sql = String.format("UPDATE tbl_images SET MainStatus = 0 WHERE ServiceID = ? AND MainStatus = ?");
		db.update(sql, new Object[] { serviceId, mainStatus });
	}

	public String save(Images newItem) {
		try {
			String str_query = String
					.format("INSERT INTO tbl_images (ImageName, MainStatus, ServiceID) VALUES (?, ?, ?)");
			int rowaccept = db.update(str_query,
					new Object[] { newItem.getImageName(), newItem.getMainStatus(), newItem.getServiceID() });
			if (rowaccept == 1) {
				return "success";
			}
			return "fail";
		} catch (Exception e) {
			return "insert exception data";
		}
	}

	public Images findById(int id) {
		String str_query = String.format("select * from tbl_images where ImageId=?");
		return db.queryForObject(str_query, new Image_mapper(), new Object[] { id });
	}

	public String deleteProImg(int id) {
		String str_query = String.format("DELETE FROM tbl_images WHERE ImageId = ?");
		try {
			int rowAccept = db.update(str_query, new Object[] { id });
			if (rowAccept == 1) {
				return "success";
			}
			return "failed";
		} catch (Exception e) {
			// Optionally, log the exception here
			e.printStackTrace();
		}
		return "delete exception data";
	}
	

	public List<Images> findAllBanner() {
	    String sql = "SELECT * FROM tbl_images WHERE BannerID IS NOT NULL";
	    return db.query(sql, new Image_mapper());
	}

}
