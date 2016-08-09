package com.accesso.challengeladder.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.accesso.challengeladder.model.Ranking;
import com.accesso.challengeladder.utils.DBHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

public class RankingService
{
	private static final Logger logger = Logger.getLogger(RankingService.class.getCanonicalName());

	private ConnectionSource connectionSource;
	private Dao<Ranking, String> rankingDao;

	public RankingService() throws SQLException, IOException
	{
		DBHelper dBHelper = new DBHelper();
		ConnectionSource connectionSource = dBHelper.getConnectionSource();

		this.connectionSource = connectionSource;
		rankingDao = DaoManager.createDao(this.connectionSource, Ranking.class);
	}

	public List<Ranking> getRanking() throws SQLException
	{
		List<Ranking> rankList = rankingDao.queryForAll();
		return rankList;
	}
}
