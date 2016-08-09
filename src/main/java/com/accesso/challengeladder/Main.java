package com.accesso.challengeladder;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.accesso.challengeladder.controller.RankingController;
import com.accesso.challengeladder.controller.RankingHistoryController;
import com.accesso.challengeladder.controller.UserController;
import com.accesso.challengeladder.utils.CorsFilters;

public class Main
{

	private static final Logger logger = Logger.getLogger(Main.class.getCanonicalName());

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException
	{

		logger.debug("Starting...");

		CorsFilters.apply();

		new UserController();

		new RankingController();

		new RankingHistoryController();

	}
}