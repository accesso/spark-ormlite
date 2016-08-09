package com.accesso.challengeladder.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public class User
{

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField
	private String name;

	@DatabaseField
	private String password;

	@DatabaseField
	private String salt;

	@DatabaseField
	private String email;

	@DatabaseField(columnName = "admin_flag")
	private String adminFlag;

	@DatabaseField(columnName = "active_flag")
	private String activeFlag;

	public User()
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

	public String getPassword()
	{
		return password;
	}

	public String getSalt()
	{
		return salt;
	}

	public String getEmail()
	{
		return email;
	}

	public String getAdminFlag()
	{
		return adminFlag;
	}

	public String getActiveFlag()
	{
		return activeFlag;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setSalt(String salt)
	{
		this.salt = salt;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public void setAdminFlag(String adminFlag)
	{
		this.adminFlag = adminFlag;
	}

	public void setActiveFlag(String activeFlag)
	{
		this.activeFlag = activeFlag;
	}
}