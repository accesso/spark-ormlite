package com.accesso.challengeladder.services;

import com.accesso.challengeladder.model.User;
import com.accesso.challengeladder.utils.DBHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RankingService
{
    private static final Logger logger = Logger.getLogger(UserService.class
            .getCanonicalName());

    private ConnectionSource connectionSource;
    private Dao<User, String> userDao;

    public RankingService() throws SQLException, IOException
    {
        DBHelper dBHelper = new DBHelper();
        ConnectionSource connectionSource = dBHelper.getConnectionSource();

        this.connectionSource = connectionSource;
        userDao = DaoManager.createDao(this.connectionSource, Ranking.class);
    }

    public List<User> getRanking() throws SQLException
    {
        List<> rankList = userDao.queryForAll();
        return rankList;
    }
}
