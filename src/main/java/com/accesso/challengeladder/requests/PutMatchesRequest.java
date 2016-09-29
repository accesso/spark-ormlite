package com.accesso.challengeladder.requests;

/**
 * @author oscar.lopez
 *
 *         Sep 27, 2016
 */
public class PutMatchesRequest
{
    private Integer matchId;
    private Integer creatorScore;
    private Integer opponentScore;

    public PutMatchesRequest(Integer matchId, Integer creatorScore, Integer opponentScore)
    {
        this.matchId = matchId;
        this.creatorScore = creatorScore;
        this.opponentScore = opponentScore;
    }

    public Integer getMatchId()
    {
        return matchId;
    }

    public Integer getCreatorScore()
    {
        return creatorScore;
    }

    public Integer getOpponentScore()
    {
        return opponentScore;
    }

    public void setMatchId(Integer matchId)
    {
        this.matchId = matchId;
    }

    public void setCreatorScore(Integer creatorScore)
    {
        this.creatorScore = creatorScore;
    }

    public void setOpponentScore(Integer opponentScore)
    {
        this.opponentScore = opponentScore;
    }

    @Override
    public String toString()
    {
        return "PutMatchesRequest [matchId=" + matchId + ", creatorScore=" + creatorScore + ", opponentScore=" + opponentScore + "]";
    }
}