package com.accesso.challengeladder.services;

import com.accesso.challengeladder.model.*;
import com.accesso.challengeladder.utils.Constants;
import com.accesso.challengeladder.utils.DBHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public Match createMatch(Integer creatorUserId, List<Integer> userIds)
    {

        // create an entry in the match table first to get an id
        Match newMatch = new Match();

        newMatch.setCreationTimestamp(new Date());
        newMatch.setVictorUser(null);
        try
        {
            newMatch.setCreatorUser(userDao.queryForId(creatorUserId.toString()));
            newMatch.setMatchStatus(matchStatusDao.queryForId(Constants.MATCH_STATUS_PENDING));
            matchDao.create(newMatch);

            if (!userIds.contains(creatorUserId))
            {
                userIds.add(creatorUserId);
            }
            // iterate over users, write an entry to match_user for each
            for (Integer userId : userIds)
            {
                MatchUser tempMatchUser = createMatchUser(userId.toString(), Integer.toString(newMatch.getId()));
                if (tempMatchUser == null)
                {
                    logger.error("Unable to create a MatchUser for userId " + userId);
                    return null;
                }
            }
        }
        catch (SQLException sqle)
        {
            logger.error(sqle);
            return null;
        }
        return newMatch;
    }

    public Match createMatch(ArrayList<String> users)
    {
        if (users == null)
            return null;

        // create an entry in the match table first to get an id
        Match newMatch = new Match();

        newMatch.setCreationTimestamp(new Date());
        newMatch.setVictorUser(null);
        try
        {
            newMatch.setCreatorUser(userDao.queryForId(users.get(0)));
        }
        catch (SQLException sqle)
        {
            logger.error(sqle);
            return null;
        }
        // check on what the match statuses will be
        // newMatch.setStatusId(0);
        newMatch.setMatchTimestamp(null);

        try
        {
            matchDao.create(newMatch);
        }
        catch (SQLException sqle)
        {
            logger.error(sqle);
            return null;
        }

        // iterate over users, write an entry to match_user for each
        for (String userId : users)
        {
            MatchUser tempMatchUser = createMatchUser(userId, Integer.toString(newMatch.getId()));
            if (tempMatchUser == null)
            {
                logger.error("Unable to create a MatchUser for userId " + userId);
                return null;
            }
        }

        return newMatch;
    }

    public Match updateMatch(String matchId, String victorId, Date matchTimestamp, String status, Date creationTimestamp, String creatorId)
    {
        // all values will be optional (except matchId), pass in null to make no change

        Match match = getMatch(matchId);

        if (match == null)
            return null;
        try
        {
            if (victorId != null)
                match.setVictorUser(userDao.queryForId(victorId));

            if (matchTimestamp != null)
                match.setMatchTimestamp(matchTimestamp);

            if (status != null)
                match.setMatchStatus(matchStatusDao.queryForId(status));

            if (creationTimestamp != null)
                match.setCreationTimestamp(creationTimestamp);

            if (creatorId != null)
                match.setCreatorUser(userDao.queryForId(creatorId));

            matchDao.update(match);
        }
        catch (SQLException sqle)
        {
            logger.error(sqle);
            return null;
        }
        return match;
    }

    public List<Match> getAllMatches() throws SQLException
    {
        List<Match> matchList = matchDao.queryForAll();
        return matchList;
    }

    public List<Match> getRecentMatches(int limit) throws SQLException
    {
        QueryBuilder<Match, String> qb = matchDao.queryBuilder();
        qb.orderBy("match_timestamp", false);
        qb.limit(Long.valueOf(limit));
        List<Match> matchList = qb.query();

        for (Match m : matchList)
        {
            userDao.refresh(m.getCreatorUser());
            userDao.refresh(m.getOpponentUser());
        }
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
