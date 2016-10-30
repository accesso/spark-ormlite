package com.accesso.challengeladder.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.accesso.challengeladder.model.Ranking;
import com.accesso.challengeladder.model.User;
import com.accesso.challengeladder.utils.DBHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

public class RankingService
{
	private static final Logger logger = Logger.getLogger(RankingService.class.getCanonicalName());

	private ConnectionSource connectionSource;
	private Dao<Ranking, String> rankingDao;
	private Dao<User, String> userDao;

	public RankingService() throws SQLException, IOException
	{
		DBHelper dBHelper = new DBHelper();
		ConnectionSource connectionSource = dBHelper.getConnectionSource();

		this.connectionSource = connectionSource;
		rankingDao = DaoManager.createDao(this.connectionSource, Ranking.class);
		userDao = DaoManager.createDao(this.connectionSource, User.class);
	}

	public List<Ranking> getRanking() throws SQLException
	{
		List<Ranking> rankList = rankingDao.queryForAll();
		for (Ranking r : rankList)
		{
			userDao.refresh(r.getUser());
		}

		return rankList;
	}

	public Ranking getUserRanking(User user) throws SQLException
	{
		List<Ranking> userRankingList = rankingDao.queryForEq("user_id", user.getId());
		if (userRankingList.size() > 0)
		{
			return userRankingList.get(0);
		}
		else
		{
			return new Ranking();
		}
	}

	public Ranking createRanking(String userId) throws SQLException
	{
		Ranking r = new Ranking();
		try
		{
			r.setUser(new User(Integer.parseInt(userId)));

			// creates a new user in the DB
			rankingDao.create(r);
		}
		catch (Exception e)
		{
			logger.error("Exception..." + e.getMessage());
			return null;
		}

		return r;
	}

	public boolean swapRankings(User user1, User user2, Integer matchId) throws IOException
	{
		try
		{
			List<Ranking> rankingUser1List = rankingDao.queryForEq("user_id", user1.getId());
			List<Ranking> rankingUser2List = rankingDao.queryForEq("user_id", user2.getId());
			if (rankingUser1List.size() != 1 || rankingUser2List.size() != 1)
			{
				return false;
			}

			Ranking rankingUser1 = rankingUser1List.get(0);
			Ranking rankingUser2 = rankingUser2List.get(0);

			rankingUser1.setUser(user2);
			rankingUser2.setUser(user1);

			rankingDao.update(rankingUser1);
			rankingDao.update(rankingUser2);

			// updates ranking history
			RankingHistoryService rankingHistoryService = new RankingHistoryService();
			rankingHistoryService.createRankingHistory(rankingUser1.getId(), rankingUser1.getUser().getId(), matchId);
			rankingHistoryService.createRankingHistory(rankingUser2.getId(), rankingUser2.getUser().getId(), matchId);

			return true;
		}
		catch (SQLException e)
		{
			logger.error("Exception swapping rankings " + e.getMessage());
			return false;
		}
	}
}
