package com.accesso.challengeladder.controller;

import com.accesso.challengeladder.services.RankingService;
import com.accesso.challengeladder.utils.JsonUtil;

import java.util.List;

import static spark.Spark.get;

public class RankingController
{
    public RankingController()
    {
        RankingService rankingService = new RankingService();

        get("/ranking", (req, res) -> {
            List<> rankings = rankingService.getRanking();
            return JsonUtil.toJson(rankings);
        });

    }
}
