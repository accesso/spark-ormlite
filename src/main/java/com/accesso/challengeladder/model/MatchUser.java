package com.accesso.challengeladder.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "match_user")
public class MatchUser
{

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "match_id", foreign = true)
    private Match match;

    @DatabaseField(columnName = "user_id", foreign = true)
    private User user;

    @DatabaseField
    private Integer score;

    public MatchUser()
    {
        // ORMLite needs a no-arg constructor
    }

    public int getId()
    {
        return this.id;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Integer getScore()
    {
        return score;
    }

    public Match getMatch()
    {
        return match;
    }

    public void setMatch(Match match)
    {
        this.match = match;
    }

    public void setScore(Integer score)
    {
        this.score = score;
    }
}