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

	private static final Logger logger = Logger.getLogger(DBHelper.class.getCanonicalName());

	public DBHelper() throws IOException
	{

		Properties prop = new Properties();
		prop.load(DBHelper.class.getClassLoader().getResourceAsStream("db.properties"));

		this.databaseUrl = prop.getProperty("db.url");
		this.userDb = prop.getProperty("db.user");
		this.passDb = prop.getProperty("db.password");
	}

	public JdbcPooledConnectionSource getConnectionSource() throws SQLException
	{
		JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(databaseUrl);
		connectionSource.setUsername(userDb);
		connectionSource.setPassword(passDb);
		connectionSource.setMaxConnectionsFree(5);
		connectionSource.setCheckConnectionsEveryMillis(5000);
		connectionSource.setMaxConnectionAgeMillis(5000);

		return connectionSource;
	}

}
