package com.accesso.challengeladder.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.accesso.challengeladder.model.Match;
import com.accesso.challengeladder.model.MatchStatus;
import com.accesso.challengeladder.model.User;
import com.accesso.challengeladder.utils.Constants;
import com.accesso.challengeladder.utils.DBHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class MatchService
{
	private ConnectionSource connectionSource;
	private Dao<Match, String> matchDao;
	private Dao<User, String> userDao;
	private Dao<MatchStatus, String> matchStatusDao;

	private static final Logger logger = Logger.getLogger(MatchService.class.getCanonicalName());

	public MatchService() throws SQLException, IOException
	{
		DBHelper dBHelper = new DBHelper();
		ConnectionSource connectionSource = dBHelper.getConnectionSource();

		this.connectionSource = connectionSource;
		matchDao = DaoManager.createDao(this.connectionSource, Match.class);
		userDao = DaoManager.createDao(this.connectionSource, User.class);
		matchStatusDao = DaoManager.createDao(this.connectionSource, MatchStatus.class);
	}

	public Match getMatch(String id)
	{
		Match response;

		try
		{
			response = matchDao.queryForId(id);
		}
		catch (SQLException sqle)
		{
			logger.error(sqle);
			return null;
		}
		return response;
	}

	public List<Match> getMatchesByUser(String userId)
	{

		List<Match> matches;
		try
		{
			UserService userService = new UserService();
			User user = userService.getUser(userId);

			matches = getMatchesByUser(user);
		}
		catch (SQLException | IOException e)
		{
			logger.error(e);
			return null;
		}
		return matches;
	}

	public Match createMatch(Integer creatorUserId, Integer opponentUserId)
	{
		// create an entry in the match table first to get an id
		Match newMatch = new Match();

		newMatch.setCreationTimestamp(new Date());
		newMatch.setVictorUser(null);
		try
		{
			newMatch.setCreatorUser(userDao.queryForId(creatorUserId.toString()));
			newMatch.setOpponentUser(userDao.queryForId(opponentUserId.toString()));
			newMatch.setMatchStatus(matchStatusDao.queryForId(Constants.MATCH_STATUS_PENDING));
			matchDao.create(newMatch);
		}
		catch (SQLException sqle)
		{
			logger.error(sqle);
			return null;
		}
		return newMatch;
	}

	public Match updateMatchResults(Integer matchId, Integer creatorScore, Integer opponentScore)
	{
		Match match = null;
		try
		{
			if (creatorScore == null || opponentScore == null)
			{
				return null;
			}

			match = matchDao.queryForId(matchId.toString());

			if (match == null)
			{
				return null;
			}

			match.setOpponentScore(opponentScore);
			match.setCreatorScore(creatorScore);
			match.setMatchTimestamp(new Date());
			match.setMatchStatus(matchStatusDao.queryForId(Constants.MATCH_STATUS_COMPLETED));

			if (creatorScore > opponentScore)
			{
				match.setVictorUser(match.getCreatorUser());
				RankingService rankingService = new RankingService();
				rankingService.swapRankings(match.getCreatorUser(), match.getOpponentUser(), matchId);
			}
			else
			{
				match.setVictorUser(match.getOpponentUser());
			}

			matchDao.update(match);
		}
		catch (Exception e)
		{
			logger.error(e);
			return null;
		}
		return match;
	}

	public List<Match> getAllMatches() throws SQLException
	{
		List<Match> matchList = matchDao.queryForAll();
		return matchList;
	}

	public List<Match> getMatchesByUser(User user) throws SQLException
	{
		QueryBuilder<Match, String> matchQB = matchDao.queryBuilder();
		matchQB.where().eq("opponent_user_id", user).or().eq("creator_user_id", user);
		List<Match> matchList = matchQB.query();

		return matchList;
	}
}
