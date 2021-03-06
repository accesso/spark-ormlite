package com.accesso.challengeladder.services;

import com.accesso.challengeladder.model.Match;
import com.accesso.challengeladder.model.User;
import com.accesso.challengeladder.utils.DBHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService
{

	private static final Logger logger = Logger.getLogger(UserService.class.getCanonicalName());

	private ConnectionSource connectionSource;
	private Dao<User, String> userDao;
	private static String salt;
	private Dao<Match, String> matchDao;

	public UserService() throws SQLException, IOException
	{
		DBHelper dBHelper = new DBHelper();
		ConnectionSource connectionSource = dBHelper.getConnectionSource();

		this.connectionSource = connectionSource;
		userDao = DaoManager.createDao(this.connectionSource, User.class);
		matchDao = DaoManager.createDao(this.connectionSource, Match.class);
	}

	public boolean addUser(User newUser)
	{
		BCrypt cryptWorker = new BCrypt();

		try
		{
			salt = cryptWorker.gensalt();

			newUser.setSalt(salt);
			newUser.setPassword(cryptWorker.hashpw("pingpong", salt));

			userDao.create(newUser);
			return true;
		}
		catch (Exception e)
		{
			logger.error("Exception..." + e.getMessage());
			return false;
		}
	}

	public boolean updateUser(User updatedUser)
	{
		BCrypt cryptWorker = new BCrypt();

		try
		{
			salt = cryptWorker.gensalt();

			Integer currentUserId = updatedUser.getId();
			User currentUser = getUser(currentUserId.toString());
			currentUser.setName(updatedUser.getName());
			currentUser.setSalt(salt);
			currentUser.setPassword(updatedUser.getPassword());
			currentUser.setEmail(updatedUser.getEmail());
			currentUser.setAdminFlag(updatedUser.getAdminFlag());
			currentUser.setActiveFlag(updatedUser.getActiveFlag());
			userDao.update(currentUser);
			return true;
		}
		catch (Exception e)
		{
			logger.error("Exception..." + e.getMessage());
			return false;
		}
	}

	public User getUser(String userId) throws SQLException
	{
		User user = null;
		if (userId != null)
		{
			user = userDao.queryForId(userId);
			user = populateUserWinsLosses(user);
		}

		return user;
	}

	public User getUserByEmail(String email) throws SQLException
	{
		Map newMap = new HashMap();

		newMap.put("email", email);

		User user = null;
		if (email != null)
		{
			List<User> results = userDao.queryForEq("email", email);
			user = results.get(0);
		}
		return user;
	}

	public String validateUser(String email, String password) throws Exception
	{
		BCrypt cryptWorker = new BCrypt();

		User user = getUserByEmail(email);

		String salt = user.getSalt();

		String hashedPW = cryptWorker.hashpw(password, salt);

		if (user.getPassword().equals(hashedPW))
		{
			return Integer.toString(user.getId());
		}

		return null;
	}

	public List<User> getAllUsers() throws SQLException
	{
		List<User> userList = userDao.queryForAll();
		for (User user : userList)
		{
			user.setPassword(null);
			user.setSalt(null);
			user.setActiveFlag(null);
			user.setAdminFlag(null);
			user.setEmail(null);
		}
		return userList;
	}

	/**
	 * Queries for the match entries and calculates the number of wins and losses and stores them in the User object
	 *
	 * @param user
	 * @throws SQLException
	 * @throws IOException
	 */
	public User populateUserWinsLosses(User user)
	{
		MatchService matchService;
		try
		{
			matchService = new MatchService();

			List<Match> matchList = matchService.getMatchesByUser(user);

			int numWins = 0;
			int numLosses = 0;

			for (Match match : matchList)
			{
				if (match.getVictorUser() != null)
				{
					if (match.getVictorUser().getId() != user.getId())
					{
						numLosses++;
					}
					else
					{
						numWins++;
					}
				}
			}

			user.setNumWins(numWins);
			user.setNumLosses(numLosses);

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return user;
	}
}