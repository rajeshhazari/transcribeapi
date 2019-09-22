/**
 * 
 */
package com.c3trTranscibe.springboot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.c3trTranscibe.springboot.model.Users;

/**
 * @author rajesh
 *
 */
@Repository
public interface UserRegLogRepository extends CrudRepository<	Users, Long>{

	/*
	 * @Autowired private JdbcTemplate jdbcTemplate;
	 * 
	 * @Transactional(readOnly = true) public List<User> findAll() { return
	 * jdbcTemplate.query("select * from users", new UserRowMapper()); }
	 * 
	 * class UserRowMapper implements RowMapper<User> {
	 * 
	 * @Override public User mapRow(ResultSet rs, int rowNum) throws SQLException {
	 * User user = new User(); user.setId(rs.getInt("id"));
	 * user.setName(rs.getString("name")); user.setEmail(rs.getString("email"));
	 * return user; } }
	 */
	
}
