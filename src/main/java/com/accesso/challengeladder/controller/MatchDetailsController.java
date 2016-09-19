package com.accesso.challengeladder.controller;

import static spark.Spark.get;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.accesso.challengeladder.model.MatchUser;
import org.eclipse.jetty.http.HttpStatus;

import com.accesso.challengeladder.model.Match;
import com.accesso.challengeladder.services.MatchService;
import com.accesso.challengeladder.utils.JsonUtil;
import com.google.gson.Gson;

public class MatchDetailsController
{
    public MatchDetailsController() throws SQLException, IOException
    {
        MatchService matchService = new MatchService();

        get("/match-details/:id", (req, res) -> {
            return JsonUtil.toJson(matchService.getMatchDetails(req.params(":id")));
        });

        get("/match-details/user/:id", (req, res) -> {
            return JsonUtil.toJson(matchService.getMatchDetailsForUser(req.params(":id")));
        });

    }
}
