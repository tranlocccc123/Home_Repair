package sop.modelviews;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sop.models.Images;

public class Image_mapper implements RowMapper<Images>  {
	@Override
    public Images mapRow(ResultSet rs, int rowNum) throws SQLException {
        Images image = new Images(
            rs.getInt("ImageID"),            // imageID
            rs.getString("ImageName"),       // imageName
            rs.getInt("MainStatus"),         // mainStatus
            rs.getInt("ServiceID"),          // serviceID
            rs.getInt("UserID"),             // userID
            rs.getInt("PostID"),             // postID
            rs.getInt("BannerID"),           // bannerID
            rs.getInt("ContractItemID"),    // contractItemID
            rs.getString("Status")            // status
        );
        return image;
    }
}
