package com.accesso.challengeladder.controller;

import static spark.Spark.get;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.accesso.challengeladder.model.Ranking;
import com.accesso.challengeladder.services.RankingService;
import com.accesso.challengeladder.utils.JsonUtil;

public class RankingController
{
	public RankingController() throws SQLException, IOException
	{
		RankingService rankingService = new RankingService();

		get("/ranking", (req, res) -> {
			List<Ranking> rankings = rankingService.getRanking();
			return JsonUtil.toJson(rankings);
		});

	}
}