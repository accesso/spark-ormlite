package com.accesso.challengeladder.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.accesso.challengeladder.model.Ranking;
import com.j256.ormlite.stmt.Where;
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

	public Match createMatch(Integer creatorUserId, Integer opponentUserId)
	{
		// create an entry in the match table first to get an id
		Match newMatch = new Match();

		newMatch.setCreationTimestamp(new Date());
		newMatch.setVictorUser(null);
		try
		{
			UserService userService = new UserService();
			newMatch.setCreatorUser(userService.getUser(creatorUserId.toString()));
			newMatch.setOpponentUser(userService.getUser(opponentUserId.toString()));
			newMatch.setMatchStatus(matchStatusDao.queryForId(Constants.MATCH_STATUS_PENDING));
			matchDao.create(newMatch);
			EmailService.sendChallengeCreatedEmails(newMatch);
		}
		catch (IOException | SQLException e)
		{
			logger.error(e);
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

			RankingService rankingService = new RankingService();
			userDao.refresh(match.getCreatorUser());
			userDao.refresh(match.getOpponentUser());
			userDao.refresh(match.getVictorUser());
			Ranking creatorRanking = rankingService.getUserRanking(match.getCreatorUser());
			Ranking opponentRanking = rankingService.getUserRanking(match.getOpponentUser());
			match.getCreatorUser().setRankId(creatorRanking.getId());
			match.getOpponentUser().setRankId(opponentRanking.getId());

			if (creatorScore > opponentScore)
			{
				match.setVictorUser(match.getCreatorUser());
				EmailService.sendChallengeCompletedEmails(match);
				rankingService.swapRankings(match.getCreatorUser(), match.getOpponentUser(), matchId);
				deleteInvalidPendingMatchesByUser(match.getCreatorUser());
				deleteInvalidPendingMatchesByUser(match.getOpponentUser());
			}
			else
			{
				EmailService.sendChallengeCompletedEmails(match);
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

	public List<Match> getAllMatches(String limit, String page) throws SQLException
	{
		// default limit to 20, and page to 0
		Long lim = limit == null ? (long) Constants.DEFAULT_PAGINATION_LIMIT : Long.valueOf(limit);
		Long p = page == null ? (long) 0 : Long.valueOf(page);

		QueryBuilder<Match, String> qb = matchDao.queryBuilder();
		qb.orderBy("match_timestamp", false);
		qb.limit(lim);
		qb.offset(lim * p);

		List<Match> matchList = qb.query();

		for (Match m : matchList)
		{
			userDao.refresh(m.getCreatorUser());
			userDao.refresh(m.getOpponentUser());
		}
		return matchList;

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

	public List<Match> getMatchesByUser(User user) throws SQLException
	{
		QueryBuilder<Match, String> matchQB = matchDao.queryBuilder();
		matchQB.orderBy("match_timestamp", false);
		Where<Match, String> matchQBWhere = matchQB.where();
		matchQBWhere.eq("opponent_user_id", user)
					.or()
					.eq("creator_user_id", user);
		matchQBWhere.and();
		matchQBWhere.eq("status_id", Constants.MATCH_STATUS_COMPLETED);
		List<Match> matchList = matchQB.query();

		for (Match m : matchList)
		{
			userDao.refresh(m.getCreatorUser());
			userDao.refresh(m.getOpponentUser());
		}
		return matchList;
	}

	public List<Match> getPendingMatchesByUser(String userId)
	{

		List<Match> matches;
		try
		{
			UserService userService = new UserService();
			User user = userService.getUser(userId);

			matches = getPendingMatchesByUser(user);
		}
		catch (SQLException | IOException e)
		{
			logger.error(e);
			return null;
		}
		return matches;
	}

	public List<Match> getPendingMatchesByUser(User user) throws SQLException
	{
		QueryBuilder<Match, String> matchQB = matchDao.queryBuilder();
		matchQB.orderBy("match_timestamp", false);
		matchQB.where().eq("creator_user_id", user)
				.and()
				.eq("status_id", Constants.MATCH_STATUS_PENDING);
		List<Match> matchList = matchQB.query();

		for (Match m : matchList)
		{
			userDao.refresh(m.getCreatorUser());
			userDao.refresh(m.getOpponentUser());
		}
		return matchList;
	}

	public void deleteInvalidPendingMatchesByUser(User user) throws SQLException
	{
		QueryBuilder<Match, String> matchQB = matchDao.queryBuilder();
		matchQB.orderBy("match_timestamp", false);
		Where<Match, String> matchQBWhere = matchQB.where();
		matchQBWhere.eq("opponent_user_id", user)
				.or()
				.eq("creator_user_id", user);
		matchQBWhere.and();
		matchQBWhere.eq("status_id", Constants.MATCH_STATUS_PENDING);
		List<Match> matchList = matchQB.query();

		try
		{
			RankingService rankingService = new RankingService();
			Ranking ranking = rankingService.getUserRanking(user);
			for (Match m : matchList)
			{
				userDao.refresh(m.getCreatorUser());
				userDao.refresh(m.getOpponentUser());
				Ranking creatorRanking;
				Ranking opponentRanking;
				if (m.getCreatorUser().getId() == user.getId())
				{
					creatorRanking = ranking;
					opponentRanking = rankingService.getUserRanking(m.getOpponentUser());
				}
				else
				{
					creatorRanking = rankingService.getUserRanking(m.getCreatorUser());
					opponentRanking = ranking;
				}
				if (opponentRanking.getId() - 3 > creatorRanking.getId()) {
					m.getCreatorUser().setRankId(creatorRanking.getId());
					m.getOpponentUser().setRankId(opponentRanking.getId());
					EmailService.sendChallengeRevokedEmails(m);
					matchDao.delete(m);
				}
			}
		}
		catch (SQLException | IOException e)
		{
			logger.error(e);
		}
	}
}
