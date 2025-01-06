package sop.repositories;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sop.models.*;
import sop.modelviews.*;

@Repository
public class BannerRepository {
	@Autowired
	JdbcTemplate db;

	// Create a new Banner
	public int createBanner(Banner banner) {
		String sql = "INSERT INTO tbl_Banner (Description, CreatedAt, Status) VALUES (?, GETDATE(), ?)";
		return db.update(sql, banner.getDescription(), banner.getStatus());
	}

	// Get Banner by ID
	public Banner getBannerById(int bannerId) {
		String sql = "SELECT * FROM tbl_Banner WHERE BannerID = ?";
		return db.queryForObject(sql, new Banner_mapper(), bannerId);
	}

	// Get all Banners
	public List<Banner> getAllBanners() {
		String sql = "SELECT * FROM tbl_Banner";
		return db.query(sql, new Banner_mapper());
	}

	// Update a Banner
	public int updateBanner(Banner banner) {
		String sql = "UPDATE tbl_Banner SET Description = ?, Status = ? WHERE BannerID = ?";
		return db.update(sql, banner.getDescription(), banner.getStatus(), banner.getBannerId());
	}

	// Delete a Banner by ID
	public int deleteBanner(int bannerId) {
		String sql = "DELETE FROM tbl_Banner WHERE BannerID = ?";
		return db.update(sql, bannerId);
	}
	public List<Integer> getIDBanners() {
	    String sql = "SELECT BannerID FROM tbl_Banner";
	    return db.queryForList(sql, Integer.class); // Returns a list of BannerIDs
	}

	public List<Images> getImagesByBannerCode() {
	    // Get the list of BannerIDs
	    List<Integer> bannerIds = getIDBanners();

	    // List to store all images related to banners
	    List<Images> imagesList = new ArrayList<>();

	    // Loop through each BannerID and query the tbl_images table
	    for (Integer bannerId : bannerIds) {
	        String sql = "SELECT * FROM tbl_images WHERE BannerID = ?";
	        // Use ImageMapper to map results from the database
	        List<Images> images = db.query(sql, new Image_mapper(), bannerId);
	        imagesList.addAll(images); // Add all images to the result list
	    }

	    return imagesList; // Return the list of all images related to the BannerIDs
	}

	

}
