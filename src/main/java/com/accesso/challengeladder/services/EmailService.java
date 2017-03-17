package com.accesso.challengeladder.services;

import java.io.IOException;

import com.accesso.challengeladder.model.Match;
import com.accesso.challengeladder.utils.Templates;
import com.sendgrid.*;
import org.apache.log4j.Logger;

public class EmailService
{

    private static final Logger logger = Logger.getLogger(UserService.class.getCanonicalName());
    private static final String API_KEY = "checkin_null";
	private static final String SYSTEM_EMAIL_ADDRESS = "test@accesso.com";
	private static final String SYSTEM_EMAIL_NAME = "accesso Ping Pong Ladder";

    public static boolean sendEmail(String subject, String toEmail, String message)
    {
        Email from = new Email(SYSTEM_EMAIL_ADDRESS, SYSTEM_EMAIL_NAME);
        Email to = new Email(toEmail);
        Content content = new Content("text/html", message);
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            Response response = sg.api(request);
            logger.debug(response.statusCode);
            logger.debug(response.body);
            logger.debug(response.headers);

            return true;
        } catch (IOException ex) {
            return false;
        }
    }

	public static void sendChallengeCreatedEmails(Match match)
	{
		EmailService.sendEmail(
			Templates.getChallengeCreatedSubject(match.getId()),
			match.getCreatorUser().getEmail(),
			Templates.getChallengeCreatedTemplate(match)
		);
		EmailService.sendEmail(
			Templates.getChallengeCreatedSubject(match.getId()),
			match.getOpponentUser().getEmail(),
			Templates.getChallengeCreatedTemplate(match)
		);

	}

	public static void sendChallengeRevokedEmails(Match match)
	{
		EmailService.sendEmail(
			Templates.getChallengeRevokedSubject(match.getId()),
			match.getCreatorUser().getEmail(),
			Templates.getChallengeRevokedTemplate(match)
		);
		EmailService.sendEmail(
			Templates.getChallengeRevokedSubject(match.getId()),
			match.getOpponentUser().getEmail(),
			Templates.getChallengeRevokedTemplate(match)
		);

	}

	public static void sendChallengeCompletedEmails(Match match)
	{
		EmailService.sendEmail(
			Templates.getChallengeCompletedSubject(match.getId()),
			match.getCreatorUser().getEmail(),
			Templates.getChallengeCompletedTemplate(match)
		);
		EmailService.sendEmail(
			Templates.getChallengeCompletedSubject(match.getId()),
			match.getOpponentUser().getEmail(),
			Templates.getChallengeCompletedTemplate(match)
		);

	}

}
