package com.accesso.challengeladder.requests;

/**
 * @author oscar.lopez
 *
 *         Aug 11, 2016
 */
public class PostMatchesRequest
{

	private Integer creatorUserId;
	private Integer oponentUserId;

	public PostMatchesRequest(Integer creatorUserId, Integer oponentUserId)
	{
		this.creatorUserId = creatorUserId;
		this.oponentUserId = oponentUserId;
	}

	public Integer getCreatorUserId()
	{
		return creatorUserId;
	}

	public Integer getOponentUserId()
	{
		return oponentUserId;
	}

	public void setCreatorUserId(Integer creatorUserId)
	{
		this.creatorUserId = creatorUserId;
	}

	public void setOponentUserId(Integer oponentUserId)
	{
		this.oponentUserId = oponentUserId;
	}

	@Override
	public String toString()
	{
		return "PostMatchesRequest [creatorUserId=" + creatorUserId + ", oponentUserId=" + oponentUserId + "]";
	}
}