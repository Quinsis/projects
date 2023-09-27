package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.repositories.*;
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
public class PlayOffMatchService {
    @Data
    public static class MatchInfo {
        public PlayOffMatchService.MatchInfo.TeamInfo teamOwner;
        public PlayOffMatchService.MatchInfo.TeamInfo teamGuest;
        public String date;
        public PlayOffStage playOffStage;
        public Tournament tournament;
        public Match match;
        public int tour;

        @Data
        public static class TeamInfo {
            public Team team;
            public TeamMatchStat stat;
        }
    }

    private List<PlayOffMatchService.MatchInfo> matchInfos = new ArrayList<>();
    private final PlayOffMatchRepository playOffMatchRepository;
    private final PlayOffStageRepository playOffStageRepository;
    private final TeamMatchStatRepository teamMatchStatRepository;
    private final TeamRepository teamRepository;
    private final PlayOffStageService playOffStageService;
    private final MatchRepository matchRepository;

    public List<PlayOffMatch> getPlayOffMatchesByTournamentId(int id) {
        List<PlayOffMatch> playOffMatches = new ArrayList<>();
        for (PlayOffMatch playOffMatch : this.playOffMatchRepository.findAll()) {
            for (PlayOffStage playOffStage : this.playOffStageRepository.findAll()) {
                if (playOffStage.getId() == playOffMatch.getPlayOffStageId() && playOffStage.getPlayOff().getTournament().getId() == id) {
                    playOffMatches.add(playOffMatch);
                    break;
                }
            }
        }
        return playOffMatches;
    }

    public List<MatchInfo> getMatchInfos() {
        List<PlayOffStage> playOffStages = playOffStageRepository.findAll();
        List<PlayOffMatch> playOffMatches = playOffMatchRepository.findAll();
        List<TeamMatchStat> teamMatchStats = teamMatchStatRepository.findAll();
        List<Team> teams = teamRepository.findAll();
        List<Match> matches = matchRepository.findAll();
        List<MatchInfo> matchInfos = new ArrayList<>();
        for (PlayOffStage playOffStage : playOffStages) {
            for (PlayOffMatch playOffMatch : playOffMatches) {
                if (!playOffMatch.getMatch().getDate().equals("1970-01-01")) {
                    PlayOffMatchService.MatchInfo matchInfo = new PlayOffMatchService.MatchInfo();
                    for (TeamMatchStat owner : teamMatchStats) {
                        if (owner.getMatchId() == playOffMatch.getMatch().getId()) {
                            for (Team team : teams) {
                                if (team.getId() == owner.getTeamId()) {
                                    matchInfo.teamOwner = new PlayOffMatchService.MatchInfo.TeamInfo();
                                    matchInfo.teamOwner.team = team;
                                    matchInfo.teamOwner.stat = owner;
                                    matchInfo.playOffStage = playOffStageService.getPlayOffStagesById(playOffMatch.getPlayOffStageId());
                                    matchInfo.match = playOffMatch.getMatch();
                                    matchInfo.tournament = playOffStage.getPlayOff().getTournament();
                                }
                            }
                            break;
                        }
                    }
                    for (TeamMatchStat guest : teamMatchStats) {
                        if (guest.getMatchId() == playOffMatch.getMatch().getId() && guest != matchInfo.teamOwner.getStat()) {
                            for (Team team : teams) {
                                if (team.getId() == guest.getTeamId()) {
                                    matchInfo.teamGuest = new PlayOffMatchService.MatchInfo.TeamInfo();
                                    matchInfo.teamGuest.team = team;
                                    matchInfo.teamGuest.stat = guest;
                                }
                            }
                            break;
                        }
                    }
                    for (Match match : matches) {
                        if (match == playOffMatch.getMatch()) {
                            matchInfo.date = match.getDate();
                            break;
                        }
                    }
                    matchInfos.add(matchInfo);
                }
            }
            break;
        }
        return sortMatchInfos(matchInfos);
    }

    public PlayOffMatchService.MatchInfo getMatchInfoById(int id) {
        for (PlayOffMatchService.MatchInfo matchInfo : getMatchInfos()) {
            if (matchInfo.getMatch().getId() == id) {
                return matchInfo;
            }
        }
        return null;
    }

    public List<PlayOffMatchService.MatchInfo> getInfoOfMatchesByTournament(int tournamentId) {
        List<PlayOffStage> playOffStages = playOffStageRepository.findAll();
        List<PlayOffMatch> playOffMatches = playOffMatchRepository.findAll();
        List<TeamMatchStat> teamMatchStats = teamMatchStatRepository.findAll();
        List<Team> teams = teamRepository.findAll();
        List<Match> matches = matchRepository.findAll();
        List<MatchInfo> matchInfos = new ArrayList<>();
        for (PlayOffStage playOffStage : playOffStages) {
            if (playOffStage.getPlayOff().getTournament().getId() == tournamentId) {
                for (PlayOffMatch playOffMatch : playOffMatches) {
                    if (playOffMatch.getPlayOffStageId() == playOffStage.getId()) {
                        PlayOffMatchService.MatchInfo matchInfo = new PlayOffMatchService.MatchInfo();
                        for (TeamMatchStat owner : teamMatchStats) {
                            if (owner.getMatchId() == playOffMatch.getMatch().getId()) {
                                for (Team team : teams) {
                                    if (team.getId() == owner.getTeamId()) {
                                        matchInfo.teamOwner = new PlayOffMatchService.MatchInfo.TeamInfo();
                                        matchInfo.teamOwner.team = team;
                                        matchInfo.teamOwner.stat = owner;
                                        matchInfo.playOffStage = playOffStageService.getPlayOffStagesById(playOffMatch.getPlayOffStageId());
                                        matchInfo.match = playOffMatch.getMatch();
                                        matchInfo.tournament = playOffStage.getPlayOff().getTournament();
                                        matchInfo.tour = playOffMatch.getTour();
                                    }
                                }
                                break;
                            }
                        }
                        for (TeamMatchStat guest : teamMatchStats) {
                            if (guest.getMatchId() == playOffMatch.getMatch().getId() && guest != matchInfo.teamOwner.getStat()) {
                                for (Team team : teams) {
                                    if (team.getId() == guest.getTeamId()) {
                                        matchInfo.teamGuest = new PlayOffMatchService.MatchInfo.TeamInfo();
                                        matchInfo.teamGuest.team = team;
                                        matchInfo.teamGuest.stat = guest;
                                        matchInfo.tour = playOffMatch.getTour();
                                    }
                                }
                                break;
                            }
                        }
                        for (Match match : matches) {
                            if (match == playOffMatch.getMatch()) {
                                matchInfo.date = match.getDate();
                                break;
                            }
                        }
                        matchInfos.add(matchInfo);
                    }
                }
                break;
            }
        }
        return sortMatchInfos(matchInfos);
    }

    public List<PlayOffMatchService.MatchInfo> getInfoOfMatchesByTournamentAndTour(int tournamentId, int tour) {
        List<PlayOffStage> playOffStages = playOffStageRepository.findAll();
        List<PlayOffMatch> playOffMatches = playOffMatchRepository.findAll();
        List<TeamMatchStat> teamMatchStats = teamMatchStatRepository.findAll();
        List<Team> teams = teamRepository.findAll();
        List<Match> matches = matchRepository.findAll();
        List<MatchInfo> matchInfos = new ArrayList<>();
        for (PlayOffStage playOffStage : playOffStages) {
            if (playOffStage.getPlayOff().getTournament().getId() == tournamentId) {
                for (PlayOffMatch playOffMatch : playOffMatches) {
                    if (playOffMatch.getPlayOffStageId() == playOffStage.getId() && playOffMatch.getTour() == tour) {
                        PlayOffMatchService.MatchInfo matchInfo = new PlayOffMatchService.MatchInfo();
                        for (TeamMatchStat owner : teamMatchStats) {
                            if (owner.getMatchId() == playOffMatch.getMatch().getId()) {
                                for (Team team : teams) {
                                    if (team.getId() == owner.getTeamId()) {
                                        matchInfo.teamOwner = new PlayOffMatchService.MatchInfo.TeamInfo();
                                        matchInfo.teamOwner.team = team;
                                        matchInfo.teamOwner.stat = owner;
                                        matchInfo.playOffStage = playOffStageService.getPlayOffStagesById(playOffMatch.getPlayOffStageId());
                                        matchInfo.match = playOffMatch.getMatch();
                                        matchInfo.tournament = playOffStage.getPlayOff().getTournament();
                                        matchInfo.tour = playOffMatch.getTour();
                                    }
                                }
                                break;
                            }
                        }
                        for (TeamMatchStat guest : teamMatchStats) {
                            if (guest.getMatchId() == playOffMatch.getMatch().getId() && guest != matchInfo.teamOwner.getStat()) {
                                for (Team team : teams) {
                                    if (team.getId() == guest.getTeamId()) {
                                        matchInfo.teamGuest = new PlayOffMatchService.MatchInfo.TeamInfo();
                                        matchInfo.teamGuest.team = team;
                                        matchInfo.teamGuest.stat = guest;
                                        matchInfo.tour = playOffMatch.getTour();
                                    }
                                }
                                break;
                            }
                        }
                        for (Match match : matches) {
                            if (match == playOffMatch.getMatch()) {
                                matchInfo.date = match.getDate();
                                break;
                            }
                        }
                        matchInfos.add(matchInfo);
                    }
                }
            }
        }
        return sortMatchInfos(matchInfos);
    }

    public List<PlayOffMatchService.MatchInfo> sortMatchInfos(List<MatchInfo> matchInfos) {
        for (int i = 0; i < matchInfos.size() - 1; i++) {
            for (int j = i + 1; j < matchInfos.size(); j++) {
                if (matchInfos.get(i).date.compareTo(matchInfos.get(j).date) < 0) {
                    Collections.swap(matchInfos, i, j);
                }
            }
        }
        return matchInfos;
    }

    public List<PlayOffMatch> getPlayOffMatchesByTournamentIdAndStageIdAndTour(int id, String stageName, int tour) {
        List<PlayOffMatch> playOffMatches = new ArrayList<>();
        for (PlayOffMatch playOffMatch : getPlayOffMatchesByTournamentId(id)) {
            for (PlayOffStage playOffStage : this.playOffStageRepository.findAll()) {
                if (playOffStage.getName().equals(stageName) && playOffStage.getPlayOff().getTournament().getId() == id && playOffMatch.getPlayOffStageId() == playOffStage.getId() && playOffMatch.getTour() == tour) {
                    playOffMatches.add(playOffMatch);
                    break;
                }
            }
        }

        List<PlayOffMatch> temp = new ArrayList<>();

        switch (stageName) {
            case "1/8 Финала":
                for (int i = 0; i < 8 - playOffMatches.size(); i++) {
                    temp.add(new PlayOffMatch());
                }
                return temp;
            case "1/4 Финала":
                for (int i = 0; i < 4 - playOffMatches.size(); i++) {
                    temp.add(new PlayOffMatch());
                }
                return temp;
            case "Полуфинал":
                for (int i = 0; i < 2 - playOffMatches.size(); i++) {
                    temp.add(new PlayOffMatch());
                }
                return temp;
            case "Финал":
                for (int i = 0; i < 1 - playOffMatches.size(); i++) {
                    temp.add(new PlayOffMatch());
                }
                return temp;
            default:
                return new ArrayList<>();
        }
    }

    public List<PlayOffMatch> getPlayOffMatchesByTournamentIdAndStageId(int id, String stageName) {
        List<PlayOffStage> playOffStages = playOffStageRepository.findAll();
        List<PlayOffMatch> playOffMatches = new ArrayList<>();
        for (PlayOffMatch playOffMatch : getPlayOffMatchesByTournamentId(id)) {
            for (PlayOffStage playOffStage : playOffStages) {
                if (playOffStage.getName().equals(stageName) && playOffStage.getPlayOff().getTournament().getId() == id && playOffMatch.getPlayOffStageId() == playOffStage.getId()) {
                    playOffMatches.add(playOffMatch);
                    break;
                }
            }
        }

        List<PlayOffMatch> temp = new ArrayList<>();

        switch (stageName) {
            case "1/8 Финала":
                for (int i = 0; i < 8 - playOffMatches.size(); i++) {
                    temp.add(new PlayOffMatch());
                }
                return temp;
            case "1/4 Финала":
                for (int i = 0; i < 4 - playOffMatches.size(); i++) {
                    temp.add(new PlayOffMatch());
                }
                return temp;
            case "Полуфинал":
                for (int i = 0; i < 2 - playOffMatches.size(); i++) {
                    temp.add(new PlayOffMatch());
                }
                return temp;
            case "Финал":
                for (int i = 0; i < 1 - playOffMatches.size(); i++) {
                    temp.add(new PlayOffMatch());
                }
                return temp;
            default:
                return new ArrayList<>();
        }
    }

    public void deleteMatch(PlayOffMatch playOffMatch) {
        playOffMatchRepository.delete(playOffMatch);
    }

    public PlayOffMatch getMatchById(int id) {
        for (PlayOffMatch playOffMatch : playOffMatchRepository.findAll()) {
            if (playOffMatch.getMatch().getId() == id) {
                return playOffMatch;
            }
        }
        return null;
    }

    public void addPlayOffMatch(PlayOffMatch playOffMatch) {
        this.playOffMatchRepository.save(playOffMatch);
    }
}
