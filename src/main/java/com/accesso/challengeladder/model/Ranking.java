package com.accesso.challengeladder.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ranking")
public class Ranking
{

	@DatabaseField(columnName = "rank_id", generatedId = true)
	private int id;

	@DatabaseField(columnName = "user_id")
	private int userId;

	@DatabaseField
	private Date timestamp;

	public Ranking()
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

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	public Date getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}

}