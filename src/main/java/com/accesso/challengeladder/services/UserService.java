package com.accesso.challengeladder.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.accesso.challengeladder.model.User;
import com.accesso.challengeladder.utils.DBHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

public class UserService {

	private static final Logger logger = Logger.getLogger(UserService.class
			.getCanonicalName());

	private ConnectionSource connectionSource;
	private Dao<User, String> userDao;

	public UserService() throws SQLException, IOException {

		DBHelper dBHelper = new DBHelper();
		ConnectionSource connectionSource = dBHelper.getConnectionSource();

		this.connectionSource = connectionSource;
		userDao = DaoManager.createDao(this.connectionSource, User.class);
	}

	public User createUser(String username, String email) {
		User user = new User();
		try {

			user.setUsername(username);
			user.setEmail(email);
			user.setCreatedAt(new Date());
			// creates a new user in the DB
			userDao.create(user);
		} catch (Exception e) {
			logger.error("Exception..." + e.getMessage());
			return null;
		}
		return user;
	}

	public User getUser(String id) throws SQLException {
		User user = userDao.queryForId(id);
		if (user != null) {
			return user;
		} else {
			return null;
		}
	}

	public List<User> getAllUsers() throws SQLException {
        List<User> userList = userDao.queryForAll();
        return userList;
    }
}
