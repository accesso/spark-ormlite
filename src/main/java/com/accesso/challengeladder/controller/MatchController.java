package com.accesso.challengeladder.controller;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import com.accesso.challengeladder.model.Match;
import com.accesso.challengeladder.requests.PostMatchesRequest;
import com.accesso.challengeladder.requests.PutMatchesRequest;
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

		get("/matches/user/:id", (req, res) -> {
			return JsonUtil.toJson(matchService.getMatchesByUser(req.params(":id")));
		});

		post("/matches", (req, res) -> {
			PostMatchesRequest postMatchesRequest = new Gson().fromJson(req.body(), PostMatchesRequest.class);

			Match match = matchService.createMatch(postMatchesRequest.getCreatorUserId(), postMatchesRequest.getOpponentUserId());

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

		put("/matches/:id", (req, res) -> {

			Integer matchId = Integer.valueOf(req.params(":id"));
			PutMatchesRequest putMatchesRequest = new Gson().fromJson(req.body(), PutMatchesRequest.class);

			if (putMatchesRequest.getCreatorScore() == putMatchesRequest.getOpponentScore())
			{
				res.status(HttpStatus.BAD_REQUEST_400);
				return "Scores should be different";
			}

			Match match = matchService.updateMatchResults(matchId, putMatchesRequest.getCreatorScore(), putMatchesRequest.getOpponentScore());

			if (match == null)
			{
				res.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
				return "Error updating match";
			}
			else
			{
				return JsonUtil.toJson(match);
			}
		});
	}
}
