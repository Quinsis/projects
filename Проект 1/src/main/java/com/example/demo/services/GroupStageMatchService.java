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
public class GroupStageMatchService {
    private final GroupStageMatchRepository groupStageMatchRepository;
    private final GroupStageRepository groupStageRepository;
    private final TeamMatchStatRepository teamMatchStatRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private List<PlayOffMatchService.MatchInfo> matchInfos = new ArrayList<>();

    public GroupStage getGroupStageByMatchId(int id) {
        for (GroupStageMatch groupStageMatch : this.groupStageMatchRepository.findAll()) {
            if (groupStageMatch.getMatch().getId() == id) {
                return groupStageMatch.getGroupStage();
            }
        }
        return null;
    }

    public List<PlayOffMatchService.MatchInfo> getMatchInfos() {
        List<GroupStage> groupStages = groupStageRepository.findAll();
        List<GroupStageMatch> groupStageMatches = groupStageMatchRepository.findAll();
        List<TeamMatchStat> teamMatchStats = teamMatchStatRepository.findAll();
        List<Team> teams = teamRepository.findAll();
        List<Match> matches = matchRepository.findAll();
        matchInfos = new ArrayList<>();
        for (GroupStage groupStage : groupStages) {
            for (GroupStageMatch groupStageMatch : groupStageMatches) {
                if (!groupStageMatch.getMatch().getDate().equals("1970-01-01")) {
                    if (groupStageMatch.getGroupStage().getId() == groupStage.getId()) {
                        PlayOffMatchService.MatchInfo matchInfo = new PlayOffMatchService.MatchInfo();
                        for (TeamMatchStat owner : teamMatchStats) {
                            if (owner.getMatchId() == groupStageMatch.getMatch().getId()) {
                                for (Team team : teams) {
                                    if (team.getId() == owner.getTeamId()) {
                                        matchInfo.teamOwner = new PlayOffMatchService.MatchInfo.TeamInfo();
                                        matchInfo.teamOwner.team = team;
                                        matchInfo.teamOwner.stat = owner;
                                        matchInfo.tournament = groupStage.getTournament();
                                        matchInfo.match = groupStageMatch.getMatch();
                                    }
                                }
                                break;
                            }
                        }
                        for (TeamMatchStat guest : teamMatchStats) {
                            if (guest.getMatchId() == groupStageMatch.getMatch().getId() && guest != matchInfo.teamOwner.getStat()) {
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
                            if (match == groupStageMatch.getMatch()) {
                                matchInfo.date = match.getDate();
                                break;
                            }
                        }
                        matchInfos.add(matchInfo);
                    }
                }
            }
        }
        return sortMatchInfos();
    }
    public List<PlayOffMatchService.MatchInfo> getInfoOfMatchesByGroupAndTournament(int tournamentId, int groupId) {
        List<GroupStage> groupStages = groupStageRepository.findAll();
        List<GroupStageMatch> groupStageMatches = groupStageMatchRepository.findAll();
        List<TeamMatchStat> teamMatchStats = teamMatchStatRepository.findAll();
        List<Team> teams = teamRepository.findAll();
        List<Match> matches = matchRepository.findAll();
        matchInfos = new ArrayList<>();
        for (GroupStage groupStage : groupStages) {
            if (groupStage.getTournament().getId() == tournamentId && groupStage.getId() == groupId) {
                for (GroupStageMatch groupStageMatch : groupStageMatches) {
                    if (!groupStageMatch.getMatch().getDate().equals("1970-01-01")) {
                        if (groupStageMatch.getGroupStage().getId() == groupStage.getId()) {
                            PlayOffMatchService.MatchInfo matchInfo = new PlayOffMatchService.MatchInfo();
                            for (TeamMatchStat owner : teamMatchStats) {
                                if (owner.getMatchId() == groupStageMatch.getMatch().getId()) {
                                    for (Team team : teams) {
                                        if (team.getId() == owner.getTeamId()) {
                                            matchInfo.teamOwner = new PlayOffMatchService.MatchInfo.TeamInfo();
                                            matchInfo.teamOwner.team = team;
                                            matchInfo.teamOwner.stat = owner;
                                            matchInfo.tournament = groupStage.getTournament();
                                            matchInfo.match = groupStageMatch.getMatch();
                                        }
                                    }
                                    break;
                                }
                            }
                            for (TeamMatchStat guest : teamMatchStats) {
                                if (guest.getMatchId() == groupStageMatch.getMatch().getId() && guest != matchInfo.teamOwner.getStat()) {
                                    for (Team team : teamRepository.findAll()) {
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
                                if (match == groupStageMatch.getMatch()) {
                                    matchInfo.date = match.getDate();
                                    break;
                                }
                            }
                            matchInfos.add(matchInfo);
                        }
                    }
                }
                break;
            }
        }
        return sortMatchInfos();
    }

    public PlayOffMatchService.MatchInfo getMatchInfoById(int id) {
        for (PlayOffMatchService.MatchInfo matchInfo : matchInfos) {
            if (matchInfo.teamOwner.stat.getMatchId() == id) {
                return matchInfo;
            }
        }
        return null;
    }

    public List<PlayOffMatchService.MatchInfo> sortMatchInfos() {
        for (int i = 0; i < matchInfos.size() - 1; i++) {
            for (int j = i + 1; j < matchInfos.size(); j++) {
                if (matchInfos.get(i).date.compareTo(matchInfos.get(j).date) < 0) {
                    Collections.swap(matchInfos, i, j);
                }
            }
        }
        return matchInfos;
    }

    public void addGroupStageMatch(GroupStageMatch groupStageMatch) {
        this.groupStageMatchRepository.save(groupStageMatch);
    }
}
