package com.accesso.challengeladder.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.accesso.challengeladder.model.RankingHistory;
import com.accesso.challengeladder.utils.DBHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

public class RankingHistoryService
{

	private static final Logger logger = Logger.getLogger(RankingHistoryService.class.getCanonicalName());

	private ConnectionSource connectionSource;
	private Dao<RankingHistory, String> rankingHistoryDao;

	public RankingHistoryService() throws SQLException, IOException
	{

		DBHelper dBHelper = new DBHelper();
		ConnectionSource connectionSource = dBHelper.getConnectionSource();

		this.connectionSource = connectionSource;
		rankingHistoryDao = DaoManager.createDao(this.connectionSource, RankingHistory.class);
	}

	public RankingHistory createRankingHistory(int ranking, int userId, int matchId)
	{
		RankingHistory rankingHistory = new RankingHistory();

		try
		{
			rankingHistory.setMatchId(matchId);
			rankingHistory.setRanking(ranking);
			rankingHistory.setUserId(userId);
			// creates a new rankingHistory in the DB
			rankingHistoryDao.create(rankingHistory);
		}
		catch (Exception e)
		{
			logger.error("Exception..." + e.getMessage());
			return null;
		}
		return rankingHistory;
	}

	public List<RankingHistory> getAll() throws SQLException
	{
		List<RankingHistory> rankingHistoryList = rankingHistoryDao.queryForAll();
		return rankingHistoryList;
	}
}
