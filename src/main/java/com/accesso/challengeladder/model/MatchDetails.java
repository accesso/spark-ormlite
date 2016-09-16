package com.accesso.challengeladder.model;

import java.util.List;

public class MatchDetails {
    private Match match;
    private List<MatchUser> matchUserList;

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public List<MatchUser> getMatchUserList() {
        return matchUserList;
    }

    public void setMatchUserList(List<MatchUser> matchUserList) {
        this.matchUserList = matchUserList;
    }
}
