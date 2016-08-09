package com.accesso.challengeladder.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "match_user")
public class MatchUser
{

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(columnName = "match_id")
	private int matchId;

	@DatabaseField(columnName = "user_id")
	private int userId;

	@DatabaseField
	private int score;

	public MatchUser()
	{
		// ORMLite needs a no-arg constructor
	}

	public int getId()
	{
		return this.id;
	}

	public int getMatchId()
	{
		return matchId;
	}

	public int getUserId()
	{
		return userId;
	}

	public int getScore()
	{
		return score;
	}

	public void setMatchId(int matchId)
	{
		this.matchId = matchId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	public void setScore(int score)
	{
		this.score = score;
	}
}