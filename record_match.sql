#
# Stored procedure used to record a completed match
#
DELIMITER $$

USE `accesso_table_tennis`$$

DROP PROCEDURE IF EXISTS `record_match`$$

CREATE DEFINER=`dba`@`172.16.%` PROCEDURE `record_match`(IN creatorUserId INT, IN opponentUserId INT, IN creatorScore INT, IN opponentScore INT)
BEGIN 
DECLARE match_id INT; 
DECLARE winner_user_id INT; 
DECLARE initial_creator_rank INT; 
DECLARE initial_opponent_rank INT; 
DECLARE dupe_match INT;

# make sure it is a valid match (i.e. winner is moving up) 
SELECT rank_id FROM ranking WHERE user_id=creatorUserId INTO initial_creator_rank; 
SELECT rank_id FROM ranking WHERE user_id=opponentUserId INTO initial_opponent_rank; 

IF initial_creator_rank >= initial_opponent_rank 
THEN signal SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid match, creator is higher rank'; 
END IF; 

# dupe match check
SELECT '1' FROM `match` WHERE creator_user_id=creatorUserId AND opponent_user_id=opponentUserId AND match_timestamp > DATE_ADD(NOW(), INTERVAL -24 HOUR) INTO dupe_match;
IF dupe_match = '1' 
THEN signal SQLSTATE '45000' SET MESSAGE_TEXT = 'Duplicate match'; 
END IF;

# set the winner_user_id
SET winner_user_id = opponentUserId;
IF creatorScore > opponentScore THEN
BEGIN
SET winner_user_id = creatorUserId;
END;
END IF; 

# create the match 
INSERT INTO `match` (match_timestamp, victor_user_id, status_id, creator_user_id, opponent_user_id, creator_score, opponent_score) VALUES (NOW(), winner_user_id, 2, creatorUserId, opponentUserId, creatorScore, opponentScore); 
SET match_id = LAST_INSERT_ID(); 
# match result for both players 
#INSERT INTO match_user (match_id, user_id, score) VALUES (match_id, winnerUserId, winnerScore); 
#INSERT INTO match_user (match_id, user_id, score) VALUES (match_id, loserUserId, loserScore); 
# do we need to adjust rank? 
IF creatorScore > opponentScore THEN 
#creator wins
BEGIN 
UPDATE ranking SET user_id=creatorUserId,TIMESTAMP=NOW() WHERE rank_id=initial_opponent_rank; 
UPDATE ranking SET user_id=opponentUserId,TIMESTAMP=NOW() WHERE rank_id=initial_creator_rank; 
# add to rank history 
INSERT INTO ranking_history (ranking, user_id, match_id) VALUES (initial_opponent_rank, creatorUserId, match_id); 
INSERT INTO ranking_history (ranking, user_id, match_id) VALUES (initial_creator_rank, opponentUserId, match_id); 
END;
END IF; 
END$$

DELIMITER ;