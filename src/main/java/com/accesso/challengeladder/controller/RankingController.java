package com.accesso.challengeladder.controller;

import static spark.Spark.get;
import static spark.Spark.post;

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

        get("/ranking/user/:id", (req, res) -> {
            Ranking ranking = rankingService.getUserRanking(req.params("id"));
            return JsonUtil.toJson(ranking);
        });

        post("/ranking", (req, res) -> rankingService.createRanking(req.queryParams("userId")), JsonUtil.json());
    }
}
