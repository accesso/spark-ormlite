package com.accesso.challengeladder.controller;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import com.accesso.challengeladder.model.Match;
import com.accesso.challengeladder.requests.PostMatchesRequest;
import com.accesso.challengeladder.services.MatchService;
import com.accesso.challengeladder.utils.JsonUtil;
import com.google.gson.Gson;

public class MatchController
{
	public MatchController() throws SQLException, IOException
	{
		MatchService matchService = new MatchService();

		get("/matches", (req, res) -> {
			List<Match> rankings = matchService.getAllMatches();
			return JsonUtil.toJson(rankings);
		});

		// get information about a match
		get("/matches/:id", (req, res) -> {
			return JsonUtil.toJson(matchService.getMatch(req.params(":id")));
		});

		// get all users in a match
		get("/matches/:id/users", (req, res) -> {
			return JsonUtil.toJson(matchService.getMatchUsers(req.params(":id")));
		});

		post(
				"/matches",
				(req, res) -> {
					PostMatchesRequest postMatchesRequest = new Gson().fromJson(req.body(), PostMatchesRequest.class);
					postMatchesRequest.getCreatorUserId();
					postMatchesRequest.getOponentUserId();

					Match match = matchService.createMatch(postMatchesRequest.getCreatorUserId(),
							new ArrayList<Integer>(Arrays.asList(postMatchesRequest.getOponentUserId())));

					if (match == null)
					{
						res.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
						return "Error creating match";
					}
					else
					{
						return JsonUtil.toJson(match);
					}
				});

		// TODO params are passed as a json string in the body entity
		put("/matches/:id",
				(req, res) -> {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					return JsonUtil.toJson(matchService.updateMatch(req.queryParams("matchId"), req.queryParams("victorId"),
							sdf.parse(req.queryParams("matchTimestamp")), req.queryParams("status"), sdf.parse(req.queryParams("creationTimestamp")),
							req.queryParams("creatorId")));
				});
	}
}
