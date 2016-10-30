package com.accesso.challengeladder.utils;

import com.accesso.challengeladder.model.Match;

public class Templates
{
	public static String getChallengeCreatedSubject(int matchId)
	{
		return "Challenge #" + matchId + " is now open!";
	}

	public static String getChallengeRevokedSubject(int matchId)
	{
		return "Challenge #" + matchId + " has been revoked!";
	}

	public static String getChallengeCompletedSubject(int matchId)
	{
		return "Challenge #" + matchId + " has been completed!";
	}

	public static String getChallengeCreatedTemplate(Match match)
	{
		String template = "<div>"
			+ "(" + match.getCreatorUser().getRankId() + ") "
			+ match.getCreatorUser().getName()
			+ " challenged ("
			+ match.getOpponentUser().getRankId() + ") "
			+ match.getOpponentUser().getName()
			+ "</div>";
		template += getContactFooter();
		return template;
	}

	public static String getChallengeRevokedTemplate(Match match)
	{
		String template = "<div>"
			+ "Challenge between "
			+ match.getCreatorUser().getName()
			+ " and "
			+ match.getOpponentUser().getName()
			+ " has been revoked!"
			+ "</div>"
			+ "<br>"
			+ "<div>"
			+ "New Ranks:"
			+ "</div>"
			+ "<br>"
			+ "<div>"
			+ match.getCreatorUser().getName()
			+ ": " + match.getCreatorUser().getRankId()
			+ "</div>"
			+ "<br>"
			+ "<div>"
			+ match.getOpponentUser().getName()
			+ ": " + match.getOpponentUser().getRankId()
			+ "</div>";
		template += getContactFooter();
		return template;
	}

	public static String getChallengeCompletedTemplate(Match match)
	{
		String template =
			"<div>"
			+ "(" + match.getCreatorUser().getRankId() + ") "
			+ match.getCreatorUser().getName()
			+ " challenged ("
			+ match.getOpponentUser().getRankId() + ") "
			+ match.getOpponentUser().getName()
			+ "</div>"
			+ "<br>"
			+ "<div>";
		if (match.getVictorUser().getId() == match.getCreatorUser().getId())
		{
			template +=
				match.getCreatorUser().getName()
				+ " def "
				+ match.getOpponentUser().getName()
				+ ", "
				+ match.getCreatorScore()
				+ "-"
				+ match.getOpponentScore();
		}
		else
		{
			template +=
				match.getOpponentUser().getName()
				+ " def "
				+ match.getCreatorUser().getName()
				+ ", "
				+ match.getOpponentScore()
				+ "-"
				+ match.getCreatorScore();
		}
		template += "</div>";
		template += getContactFooter();
		return template;
	}

	private static String getContactFooter()
	{
		return
			"<br"
			+ "<div>"
			+ "<a href='"
			+ Constants.PING_PONG_LADDER_URL
			+ "'>"
			+ "accesso Ping Pong Ladder"
			+ "</a>";
	}
}
