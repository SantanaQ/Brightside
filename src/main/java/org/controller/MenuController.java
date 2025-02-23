package org.controller;

import org.model.match.Match;
import org.saveload.Persistence;

public class MenuController {

    public MatchController loadMatch(Persistence repo, String matchIdentification)
    {
        Match match = repo.load(matchIdentification);
        return new MatchController(match);
    }

}


