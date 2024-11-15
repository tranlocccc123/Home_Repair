package sop.repositories;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sop.models.*;
import sop.modelviews.*;

@Repository
public class PostRepository {
	@Autowired
	JdbcTemplate db;

	// READ
	public List<Posts> getAllPosts() {
		String sql = "SELECT * FROM tbl_posts";
		return db.query(sql, new Post_mapper());
	}

	public Posts getPostById(int postId) {
		String sql = "SELECT * FROM tbl_posts WHERE PostID = ?";
		return db.queryForObject(sql, new Post_mapper(), postId);
	}

	// UPDATE
	public int updatePost(Posts post) {
		String sql = "UPDATE tbl_posts SET Title = ?, Content = ? WHERE PostID = ?";
		return db.update(sql, post.getTitle(), post.getContent(), post.getPostId());
	}

	// DELETE
	public int deletePost(int postId) {
		String sql = "DELETE FROM tbl_posts WHERE PostID = ?";
		return db.update(sql, postId);
	}

	public int createPost(Posts post) {
		String sql = "INSERT INTO tbl_posts ( Title, Content) VALUES (?, ?, ?)";
		return db.update(sql, post.getUserId(), post.getTitle(), post.getContent());
	}

	// CREATE

}
