package com.accesso.challengeladder.controller;

import static spark.Spark.get;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.accesso.challengeladder.model.RankingHistory;
import com.accesso.challengeladder.services.RankingHistoryService;
import com.accesso.challengeladder.utils.JsonUtil;

public class RankingHistoryController
{
	public RankingHistoryController() throws SQLException, IOException
	{

		RankingHistoryService rankingHistoryService = new RankingHistoryService();

		// Routes, logic defined in the service
		get("/ranking_history", (req, res) -> {
			List<RankingHistory> rankingHistoryList = rankingHistoryService.getAll();
			return JsonUtil.toJson(rankingHistoryList);
		});
	}
}
