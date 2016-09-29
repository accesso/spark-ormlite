package com.accesso.challengeladder.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.accesso.challengeladder.model.Match;
import com.accesso.challengeladder.model.MatchDetails;
import com.accesso.challengeladder.model.MatchStatus;
import com.accesso.challengeladder.model.MatchUser;
import com.accesso.challengeladder.model.User;
import com.accesso.challengeladder.utils.Constants;
import com.accesso.challengeladder.utils.DBHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

public class MatchService
{
    private ConnectionSource connectionSource;
    private Dao<Match, String> matchDao;
    private Dao<User, String> userDao;
    private Dao<MatchUser, String> matchUserDao;
    private Dao<MatchStatus, String> matchStatusDao;

    private static final Logger logger = Logger.getLogger(MatchService.class.getCanonicalName());

    public MatchService() throws SQLException, IOException
    {
        DBHelper dBHelper = new DBHelper();
        ConnectionSource connectionSource = dBHelper.getConnectionSource();

        this.connectionSource = connectionSource;
        matchDao = DaoManager.createDao(this.connectionSource, Match.class);
        userDao = DaoManager.createDao(this.connectionSource, User.class);
        matchUserDao = DaoManager.createDao(this.connectionSource, MatchUser.class);
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

    public List<MatchUser> getMatchUsers(String matchId)
    {

        List<MatchUser> response;
        try
        {
            response = matchUserDao.queryForEq("match_id", matchId);
        }
        catch (SQLException sqle)
        {
            logger.error(sqle);
            return null;
        }
        return response;
    }

    public List<MatchUser> getMatchUsersByUser(String userId)
    {

        List<MatchUser> response;
        try
        {
            response = matchUserDao.queryForEq("user_id", userId);
        }
        catch (SQLException sqle)
        {
            logger.error(sqle);
            return null;
        }
        return response;
    }

    public MatchDetails getMatchDetails(String matchId) throws SQLException
    {
        List<MatchUser> matchUserList = getMatchUsers(matchId);
        Match match = getMatch(matchId);
        MatchDetails response = new MatchDetails();
        response.setMatch(match);
        response.setMatchUserList(matchUserList);
        return response;
    }

    public List<MatchDetails> getMatchDetailsForUser(String userId) throws SQLException
    {
        List<MatchDetails> matchDetailsList = new ArrayList<>();
        List<MatchUser> matchUserList = getMatchUsersByUser(userId);
        MatchDetails matchDetails;
        for (MatchUser matchUser : matchUserList)
        {
            String matchId = Integer.toString(matchUser.getMatch().getId());
            matchDetails = getMatchDetails(matchId);
            matchDetailsList.add(matchDetails);
        }
        return matchDetailsList;
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

            matchDao.update(match);

            if (creatorScore > opponentScore)
            {
                RankingService rankingService = new RankingService();
                rankingService.swapRankings(match.getCreatorUser(), match.getOpponentUser());
            }
            // TODO ranking and ranking_history
            // swapRankings(userId,userId)
            // addRankingHistory

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

    private MatchUser createMatchUser(String userId, String matchId)
    {

        MatchUser newMatchUser = new MatchUser();

        try
        {
            newMatchUser.setUser(userDao.queryForId(userId));
            newMatchUser.setMatch(matchDao.queryForId(matchId));
            matchUserDao.create(newMatchUser);
        }
        catch (SQLException sqle)
        {
            logger.error(sqle);
            return null;
        }

        return newMatchUser;
    }
}
