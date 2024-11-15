package sop.modelviews;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sop.models.*;

public class Banner_mapper implements RowMapper<Banner> {

    @Override
    public Banner mapRow(ResultSet rs, int rowNum) throws SQLException {
        Banner banner = new Banner();

        banner.setBannerId(rs.getInt("BannerID"));
        banner.setDescription(rs.getString("Description"));
        banner.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        banner.setStatus(rs.getInt("Status"));

        return banner;
    }
}
