package ru.yandex.practicum.filmorate.storage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@Component
@Qualifier("InMemoryUserDBStorage")
public class InMemoryUserDBStorage implements UserDBStorage {

	private final JdbcTemplate jdbcTemplate;

	public InMemoryUserDBStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<User> getAll() {
		SqlRowSet rs = jdbcTemplate.queryForRowSet("select id from users");
		List<User> result = new ArrayList<>();
		while(rs.next()) {
            result.add(this.get(rs.getInt("id")));
        }
		return result;
	}

	@Override
	public User get(Integer id) {

		SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * "
														+ "from users "
														+ "where id = " +
														 id);
		SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("SELECT friend_id "
													  	+ "FROM user_friends "
													  	+ "WHERE user_id = " + id);

		if(userRows.next()) {

            User user = makeUser(userRows);

            
            while(friendsRows.next()) {
    			user.getFriends().add(friendsRows.getInt("FRIEND_ID"));
    		}

            log.info("Найден пользователь: {} {}", user.getId(), user.getName());

            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return null;
        }
	}

	@Override
	public void clearAll() {
		SqlRowSet rs = jdbcTemplate.queryForRowSet("select id from users");
		while(rs.next()) {
            this.delete(rs.getInt("id"));
        }
	}

	@Override
	public boolean delete(Integer id) {
		boolean status = false;
		jdbcTemplate.execute("delete from users where id = " + id);
		SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * "
				+ "from users "
				+ "where id = " +
				 id);
		if(!userRows.next()) {
			log.info("Удален пользователь: {}", id);
			status = true;
		} else {
			log.info("Пользователь с идентификатором {} не найден.", id);
		}
		return status;
	}

	@Override
	public User edit(Integer id, User user) {
		String username = user.getName();
		String login = user.getLogin();
		if (username == null || username.isBlank()) {
			username = login;
		}
		String email = user.getEmail();
		String birthday = user.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		String sql = String.format("update users set"
				 	+ " username = '%s',"
				 	+ " login = '%s'," 
				 	+ " email = '%s',"
				 	+ " birthday = '%s'"
				 	+ " where id = '%d'", username, login, email, birthday, id);

		jdbcTemplate.execute(sql);

		return this.get(id);
	}

	@Override
	public User create(User user) {
		String username = user.getName();
		String login = user.getLogin();
		if (username == null || username.isBlank()) {
			username = login;
		}
		String email = user.getEmail();
		String birthday = user.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT max(id) as id from users");
		rs.next();
		Integer id = rs.getInt("id") + 1;

		String sql = String.format("insert into users"
								+ " values (%d, '%s','%s','%s','%s')", id, username, login, email, birthday);

		jdbcTemplate.execute(sql);
		
		

		return this.get(id);
	}

	@Override
	public List<User> getUserFriends(Integer userId) {
		List<User> friends = new ArrayList<>();
		SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("SELECT friend_id "
			  	+ "FROM user_friends "
			  	+ "WHERE user_id = " + userId);
		while (friendsRows.next()) {
			friends.add(this.get(friendsRows.getInt("friend_id")));
		}
		return friends;
	}

	@Override
	public List<User> getCommonFriends(Integer userId, Integer friendId) {
		SqlRowSet userRows = jdbcTemplate.queryForRowSet("select f1.friend_id"
				+ " from user_friends as f1"
				+ " right join user_friends as f2 on f1.friend_id = f2.friend_id"
				+ " where f1.user_id = " + userId 
				+ " and f2.user_id = " + friendId);
		List<User> friends = new ArrayList<>();
		while (userRows.next()) {
			friends.add(this.get(userRows.getInt("friend_id")));
		}
 		return friends;
	}

	@Override
	public List<User> removeFriend(Integer userId, Integer friendId) {
		List<User> userPair = new ArrayList<>();
		jdbcTemplate.execute("delete"
						   + " from user_friends"
						   + " where user_id = " + userId
						   + " and friend_id = " + friendId);
		userPair.add(this.get(userId));
		userPair.add(this.get(friendId));
		return userPair;
	}

	@Override
	public List<User> addFriend(Integer userId, Integer friendId) {
		List<User> userPair = new ArrayList<>();
		if(this.get(userId) == null || this.get(friendId) == null) {
			return userPair;
		}
		jdbcTemplate.execute("insert into user_friends (user_id, friend_id)"
				   + " values (" + userId + ", " + friendId + ")");
		userPair.add(this.get(userId));
		userPair.add(this.get(friendId));
		return userPair;
	}

	private User makeUser(SqlRowSet rs) {
		return User.builder()
        		.id(rs.getInt("id"))
                .name(rs.getString("username"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
	}
}
