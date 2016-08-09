package com.accesso.challengeladder.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "match")
public class Match
{

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(columnName = "user_id")
	private int userId;

	@DatabaseField(columnName = "match_timestamp")
	private Date matchTimestamp;

	@DatabaseField(columnName = "creation_timestamp")
	private Date creationTimestamp;

	@DatabaseField(columnName = "victor_user_id")
	private int victorUserId;

	@DatabaseField(columnName = "status_id")
	private int statusId;

	@DatabaseField(columnName = "creator_user_id")
	private int creatorUserId;

	public Match()
	{
		// ORMLite needs a no-arg constructor
	}

	public int getId()
	{
		return this.id;
	}

	public int getUserId()
	{
		return userId;
	}

	public Date getMatchTimestamp()
	{
		return matchTimestamp;
	}

	public Date getCreationTimestamp()
	{
		return creationTimestamp;
	}

	public int getVictorUserId()
	{
		return victorUserId;
	}

	public int getStatusId()
	{
		return statusId;
	}

	public int getCreatorUserId()
	{
		return creatorUserId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	public void setMatchTimestamp(Date matchTimestamp)
	{
		this.matchTimestamp = matchTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp)
	{
		this.creationTimestamp = creationTimestamp;
	}

	public void setVictorUserId(int victorUserId)
	{
		this.victorUserId = victorUserId;
	}

	public void setStatusId(int statusId)
	{
		this.statusId = statusId;
	}

	public void setCreatorUserId(int creatorUserId)
	{
		this.creatorUserId = creatorUserId;
	}

}