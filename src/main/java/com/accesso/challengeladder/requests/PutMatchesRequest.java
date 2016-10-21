package com.accesso.challengeladder.requests;

/**
 * @author oscar.lopez
 *
 *         Sep 27, 2016
 */
public class PutMatchesRequest
{
	private Integer creatorScore;
	private Integer opponentScore;

	public PutMatchesRequest(Integer creatorScore, Integer opponentScore)
	{
		this.creatorScore = creatorScore;
		this.opponentScore = opponentScore;
	}

	public Integer getCreatorScore()
	{
		return creatorScore;
	}

	public Integer getOpponentScore()
	{
		return opponentScore;
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
		return "PutMatchesRequest [creatorScore=" + creatorScore + ", opponentScore=" + opponentScore + "]";
	}
}