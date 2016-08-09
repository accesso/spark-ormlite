package com.accesso.challengeladder.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ranking_history")
public class RankingHistory
{

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField
	private int ranking;

	@DatabaseField(columnName = "user_id")
	private int userId;

	@DatabaseField(columnName = "match_id")
	private int matchId;

	@DatabaseField(columnName = "updated_at")
	private Date updatedAt;

	public RankingHistory()
	{
		// ORMLite needs a no-arg constructor
	}

	public int getId()
	{
		return this.id;
	}

	public int getRanking()
	{
		return ranking;
	}

	public int getUserId()
	{
		return userId;
	}

	public int getMatchId()
	{
		return matchId;
	}

	public Date getUpdatedAt()
	{
		return updatedAt;
	}

	public void setRanking(int ranking)
	{
		this.ranking = ranking;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	public void setMatchId(int matchId)
	{
		this.matchId = matchId;
	}

	public void setUpdatedAt(Date updatedAt)
	{
		this.updatedAt = updatedAt;
	}
}