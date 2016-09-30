package com.accesso.challengeladder.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "match")
public class Match
{

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(columnName = "match_timestamp")
	private Date matchTimestamp;

	@DatabaseField(columnName = "creation_timestamp")
	private Date creationTimestamp;

	@DatabaseField(columnName = "victor_user_id", foreign = true)
	private User victorUser;

	@DatabaseField(columnName = "status_id", foreign = true)
	private MatchStatus matchStatus;

	@DatabaseField(columnName = "creator_user_id", foreign = true)
	private User creatorUser;

	@DatabaseField(columnName = "opponent_user_id", foreign = true)
	private User opponentUser;

	@DatabaseField(columnName = "creator_score")
	private int creatorScore;

	@DatabaseField(columnName = "opponent_score")
	private int opponentScore;

	public Match()
	{
		// ORMLite needs a no-arg constructor
	}

	public int getId()
	{
		return this.id;
	}

	public Date getMatchTimestamp()
	{
		return matchTimestamp;
	}

	public Date getCreationTimestamp()
	{
		return creationTimestamp;
	}

	public void setMatchTimestamp(Date matchTimestamp)
	{
		this.matchTimestamp = matchTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp)
	{
		this.creationTimestamp = creationTimestamp;
	}

	public User getVictorUser()
	{
		return victorUser;
	}

	public MatchStatus getMatchStatus()
	{
		return matchStatus;
	}

	public User getCreatorUser()
	{
		return creatorUser;
	}

	public void setVictorUser(User victorUser)
	{
		this.victorUser = victorUser;
	}

	public void setMatchStatus(MatchStatus matchStatus)
	{
		this.matchStatus = matchStatus;
	}

	public void setCreatorUser(User creatorUser)
	{
		this.creatorUser = creatorUser;
	}

	public int getCreatorScore()
	{
		return creatorScore;
	}

	public void setCreatorScore(int creatorScore)
	{
		this.creatorScore = creatorScore;
	}

	public int getOpponentScore()
	{
		return opponentScore;
	}

	public void setOpponentScore(int opponentScore)
	{
		this.opponentScore = opponentScore;
	}

	public User getOpponentUser()
	{
		return opponentUser;
	}

	public void setOpponentUser(User opponentUser)
	{
		this.opponentUser = opponentUser;
	}
}