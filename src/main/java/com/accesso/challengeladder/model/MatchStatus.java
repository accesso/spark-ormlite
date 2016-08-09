package com.accesso.challengeladder.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "match_status")
public class MatchStatus
{

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField
	private String name;

	public MatchStatus()
	{
		// ORMLite needs a no-arg constructor
	}

	public int getId()
	{
		return this.id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}