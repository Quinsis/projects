package com.example.demo.services;

import com.example.demo.models.Match;
import com.example.demo.models.PlayOffStage;
import com.example.demo.models.TeamMatchStat;
import com.example.demo.repositories.MatchRepository;
import com.example.demo.repositories.TeamMatchStatRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class TeamMatchStatService {
    private final TeamMatchStatRepository teamMatchStatRepository;
    private final MatchRepository matchRepository;
    private final MatchService matchService;

    @Data
    public class TeamMatchesHistory {
        public ArrayList<Match> matches = new ArrayList<>();

        @Data
        public static class Match {
            enum Status { WON, DRAW, LOSS };
            public Status status;
            private int id;
            private String date;
        }
    }

    public TeamMatchStat getStatById(int id) {
        for (TeamMatchStat teamMatchStat : teamMatchStatRepository.findAll()) {
            if (teamMatchStat.getId() == id) {
               return teamMatchStat;
            }
        }
        return null;
    }

    public List<TeamMatchStat> getTeamMatchStatsByMatchId(int matchId) {
        List<TeamMatchStat> teamMatchStats = new ArrayList<>();
        for (TeamMatchStat teamMatchStat : teamMatchStatRepository.findAll()) {
            if (teamMatchStat.getMatchId() == matchId) {
                teamMatchStats.add(teamMatchStat);
            }
        }
        return teamMatchStats;
    }

    public TeamMatchesHistory getMatchesHistoryByTeamId(int id) {
        List<TeamMatchStat> teamMatchStats = teamMatchStatRepository.findAll();
        TeamMatchesHistory teamMatchesHistory = new TeamMatchesHistory();
        for (TeamMatchStat teamMatchStat : teamMatchStats) {
            if (teamMatchStat.getTeamId() == id) {
                TeamMatchesHistory.Match match = new TeamMatchesHistory.Match();

                if (!matchService.getMatchById(teamMatchStat.getMatchId()).getDate().equals("1970-01-01")) {
                    for (TeamMatchStat teamMatchStat1 : teamMatchStats) {
                        if (teamMatchStat1.getId() != teamMatchStat.getId() && teamMatchStat.getMatchId() == teamMatchStat1.getMatchId()) {
                            match.id = teamMatchStat1.getMatchId();
                            if (teamMatchStat.getGoals() > teamMatchStat1.getGoals()) {
                                match.status = TeamMatchesHistory.Match.Status.WON;
                            } else if (teamMatchStat.getGoals() == teamMatchStat1.getGoals()) {
                                match.status = TeamMatchesHistory.Match.Status.DRAW;
                            } else {
                                match.status = TeamMatchesHistory.Match.Status.LOSS;
                            }
                            for (Match match1 : matchRepository.findAll()) {
                                if (match1.getId() == teamMatchStat1.getMatchId()) {
                                    match.date = match1.getDate();
                                    break;
                                }
                            }
                        }
                    }
                    teamMatchesHistory.getMatches().add(match);
                }
            }
        }

        for (int i = 0; i < teamMatchesHistory.matches.size() - 1; i++) {
            for (int j = i + 1; j < teamMatchesHistory.matches.size(); j++) {
                if (teamMatchesHistory.matches.get(i).getDate().compareTo(teamMatchesHistory.matches.get(j).getDate()) > 0) {
                    Collections.swap(teamMatchesHistory.matches, i, j);
                }
            }
        }


        while (teamMatchesHistory.matches.size() > 5) {
            teamMatchesHistory.matches.remove(0);
        }

        return teamMatchesHistory;
    }


    public void changeStat(TeamMatchStat teamMatchStat) {
        for (TeamMatchStat teamMatchStat1 : teamMatchStatRepository.findAll()) {
            if (teamMatchStat1.getId() == teamMatchStat.getId()) {
                teamMatchStat1 = teamMatchStat;
                teamMatchStatRepository.save(teamMatchStat1);
            }
        }
    }

    public void addStat(TeamMatchStat teamMatchStat) {
        this.teamMatchStatRepository.save(teamMatchStat);
    }
}
