package com.accesso.challengeladder.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import org.apache.log4j.Logger;

public class DBHelper
{

	private String databaseUrl;
	private String userDb;
	private String passDb;
	private JdbcPooledConnectionSource connectionSource;

	private static final Logger logger = Logger.getLogger(DBHelper.class.getCanonicalName());

	private static DBHelper singleDBHelper;

	public DBHelper()
	{
		try
		{
			Properties prop = new Properties();
			prop.load(DBHelper.class.getClassLoader().getResourceAsStream("db.properties"));

			this.databaseUrl = prop.getProperty("db.url");
			this.userDb = prop.getProperty("db.user");
			this.passDb = prop.getProperty("db.password");

			connectionSource = new JdbcPooledConnectionSource(databaseUrl);
			connectionSource.setUsername(userDb);
			connectionSource.setPassword(passDb);
			connectionSource.setMaxConnectionsFree(10);
			//connectionSource.setCheckConnectionsEveryMillis(5000);
			//connectionSource.setMaxConnectionAgeMillis(5000);
		}
		catch (IOException ioe)
		{
			logger.debug("Could not load db properties");
		}
		catch (SQLException sqle)
		{
			logger.debug("Could not connect to db");
		}
	}

	public static DBHelper getInstance()
	{
		if (singleDBHelper == null)
		{
			singleDBHelper = new DBHelper();
		}
		return singleDBHelper;
	}

	public JdbcPooledConnectionSource getConnectionSource() throws SQLException
	{
		return connectionSource;
	}

}
