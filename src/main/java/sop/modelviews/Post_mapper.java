package sop.modelviews;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sop.models.*;

public class Post_mapper implements RowMapper<Posts> {

    @Override
    public Posts mapRow(ResultSet rs, int rowNum) throws SQLException {
        Posts post = new Posts();

        post.setPostId(rs.getInt("PostID"));
        post.setUserId(rs.getInt("UserID"));
        post.setTitle(rs.getString("Title"));
        post.setContent(rs.getString("Content"));
        post.setCreatedAt(rs.getTimestamp("CreatedAt"));

        return post;
    }
}
