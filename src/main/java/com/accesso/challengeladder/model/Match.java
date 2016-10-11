package com.accesso.challengeladder.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "match")
public class Match
{

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "status_id", foreign = true)
    private MatchStatus matchStatus;

    @DatabaseField(columnName = "creator_user_id", foreign = true)
    private User creatorUser;

    @DatabaseField(columnName = "opponent_user_id", foreign = true)
    private User opponentUser;

    @DatabaseField(columnName = "creator_score")
    private Integer creatorScore;

    @DatabaseField(columnName = "opponent_score")
    private Integer opponentScore;

    @DatabaseField(columnName = "victor_user_id", foreign = true)
    private User victorUser;

    @DatabaseField(columnName = "match_timestamp")
    private Date matchTimestamp;

    @DatabaseField(columnName = "creation_timestamp")
    private Date creationTimestamp;

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

    public User getOpponentUser()
    {
        return opponentUser;
    }

    public Integer getCreatorScore()
    {
        return creatorScore;
    }

    public Integer getOpponentScore()
    {
        return opponentScore;
    }

    public void setOpponentUser(User opponentUser)
    {
        this.opponentUser = opponentUser;
    }

    public void setCreatorScore(Integer creatorScore)
    {
        this.creatorScore = creatorScore;
    }

    public void setOpponentScore(Integer opponentScore)
    {
        this.opponentScore = opponentScore;
    }

}