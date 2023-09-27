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
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMatchStatRepository teamMatchStatRepository;
    private final GroupStageMatchRepository groupStageMatchRepository;
    private final GroupStageRepository groupStageRepository;
    private final GroupStageService groupStageService;
    private final PlayOffStageRepository playOffStageRepository;
    private final PlayOffMatchRepository playOffMatchRepository;
    private final PlayOffStageService playOffStageService;
    private final PlayOffService playOffService;
    private final PlayOffMatchService playOffMatchService;

    @Data
    public static class TeamInfo {
        private Team team;
        private GroupStage group;
        private GroupStats groupStats;
        private PlayOff playOff;

        @Data
        public static class GroupStats {
            private int games = 0;
            private int won = 0;
            private int draw = 0;
            private int loss = 0;
            private int points = 0;
            private int diffGoals = 0;
            private String qualifiedStatus;

            public enum QualifiedStatus {
                QUALIFIED, NON_QUALIFIED
            }
        }
    }

    public List<Team> getTeams() {
        return this.teamRepository.findAll();
    }

    public List<TeamInfo> sortTeamInfos(List<TeamInfo> teamInfos) {
        for (int i = 0; i < teamInfos.size() - 1; i++) {
            for (int j = i + 1; j < teamInfos.size(); j++) {
                if (teamInfos.get(i).getGroupStats().diffGoals < teamInfos.get(j).getGroupStats().diffGoals && teamInfos.get(i).getGroup().getId() == teamInfos.get(j).getGroup().getId()) {
                    Collections.swap(teamInfos, i, j);
                }
            }
        }
        for (int i = 0; i < teamInfos.size() - 1; i++) {
            for (int j = i + 1; j < teamInfos.size(); j++) {
                if (teamInfos.get(i).getGroupStats().points < teamInfos.get(j).getGroupStats().points && teamInfos.get(i).getGroup().getId() == teamInfos.get(j).getGroup().getId()) {
                    Collections.swap(teamInfos, i, j);
                }
            }
        }
        for (GroupStage groupStage : groupStageRepository.findAll()) {
            int i = 0;
            for (TeamInfo teamInfo : teamInfos) {
                if (teamInfo.getGroup().getId() == groupStage.getId()) {
                    if (i == 0 || i == 1) teamInfo.groupStats.qualifiedStatus = TeamInfo.GroupStats.QualifiedStatus.QUALIFIED.toString();
                    else teamInfo.groupStats.qualifiedStatus = TeamInfo.GroupStats.QualifiedStatus.NON_QUALIFIED.toString();
                    i++;
                    if (i == 4) break;
                }
            }
        }
        return teamInfos;
    }

    public List<TeamInfo> sortTeamsByName() {
        List<TeamInfo> teamInfos = getTeamInfos();
        for (int i = 0; i < teamInfos.size() - 1; i++) {
            for (int j = i + 1; j < teamInfos.size(); j++) {
                if (teamInfos.get(i).getTeam().getName().compareTo(teamInfos.get(j).getTeam().getName()) > 0) {
                    Collections.swap(teamInfos, i, j);
                }
            }
        }
        return teamInfos;
    }

    public Team getTeamByName(String name) {
        for (Team team : teamRepository.findAll()) {
            if (team.getName().equals(name)) {
                return team;
            }
        }
        return null;
    }

    public Team getTeamById(int id) {
        for (Team team : teamRepository.findAll()) {
            if (team.getId() == id) {
                return team;
            }
        }
        return null;
    }

    public List<TeamInfo> getTeamInfos() {
        List<TeamMatchStat> teamMatchStats = teamMatchStatRepository.findAll();
        List<GroupStageMatch> groupStageMatches = groupStageMatchRepository.findAll();

        ArrayList<TeamInfo> teamInfos = new ArrayList<>();
        // Проходим по всем командам
        for (Team team : teamRepository.findAll()) {
            TeamInfo teamInfo = new TeamInfo();
            teamInfo.team = team;
            teamInfo.groupStats = new TeamInfo.GroupStats();

            // Ищем одну из статистик, в которой будем нужная команда
            for (TeamMatchStat teamMatchStat : teamMatchStats) {
                if (teamMatchStat.getTeamId() == team.getId()) {
                    // Счётчик сыгранных игр в групповом этапе
                    int countMatch = 0;

                    // Проходимся по всех матчам группового этапа, чтобы собрать статистику команды за группу
                    for (GroupStageMatch groupStageMatch : groupStageMatches) {
                        if (groupStageMatch.getMatch().getId() == teamMatchStat.getMatchId()) {
                            // Записываем к какой группе принадлежит команда
                            teamInfo.setGroup(groupStageService.getGroupStageById(groupStageMatch.getGroupStage().getId()));

                            // Находим противника в текущем матче, для сравнения статистик
                            boolean isFind = false;
                            for (Team team1 : teamRepository.findAll()) {
                                for (TeamMatchStat teamMatchStat1 : teamMatchStats) {
                                    if (team1.getId() == teamMatchStat1.getTeamId() && team1.getId() != team.getId()) {
                                        for (GroupStageMatch groupStageMatch1 : groupStageMatches) {
                                            if (groupStageMatch1.getMatch().getId() == groupStageMatch.getMatch().getId() && teamMatchStat1.getMatchId() == groupStageMatch1.getMatch().getId()) {
                                                // Получаем статистику команд
                                                teamInfo.groupStats.games++; // +1 игра для команды
                                                teamInfo.groupStats.diffGoals += (teamMatchStat.getGoals() - teamMatchStat1.getGoals());
                                                if (teamMatchStat.getGoals() > teamMatchStat1.getGoals()) {
                                                    teamInfo.groupStats.won++; // +1 победа для команды
                                                    teamInfo.groupStats.points += 3; // +3 очка для команды
                                                } else if (teamMatchStat.getGoals() == teamMatchStat1.getGoals()) {
                                                    teamInfo.groupStats.draw++; // +1 ничья для команды
                                                    teamInfo.groupStats.points += 1; // +1 очко для команды
                                                } else if (teamMatchStat.getGoals() < teamMatchStat1.getGoals()) {
                                                    teamInfo.groupStats.loss++; // +1 поражение для команды
                                                    teamInfo.groupStats.points += 0;// +0 очко для команды
                                                }
                                                teamInfo.group = groupStageMatch.getGroupStage();
                                                countMatch++;
                                                isFind = true;
                                            }
                                            if (isFind) break;
                                        }
                                    }
                                    if (isFind) break;
                                }
                                if (isFind) break;
                            }
                        }
                    }
                }
            }
            teamInfos.add(teamInfo);
        }
        return sortTeamInfos(teamInfos);
    }

    public List<TeamInfo> getTeamInfosByTournamentId(int tournamentId) {
        List<TeamMatchStat> teamMatchStats = teamMatchStatRepository.findAll();
        List<GroupStageMatch> groupStageMatches = groupStageMatchRepository.findAll();
        List<Team> teams = teamRepository.findAll();

        ArrayList<TeamInfo> teamInfos = new ArrayList<>();
        // Проходим по всем командам
        for (Team team : teamRepository.findAll()) {
            TeamInfo teamInfo = new TeamInfo();
            teamInfo.team = team;
            teamInfo.groupStats = new TeamInfo.GroupStats();
            teamInfo.playOff = playOffService.getPlayOffByTournamentId(tournamentId);

            TeamInfo temp = new TeamInfo();

            // Ищем одну из статистик, в которой будем нужная команда
            for (TeamMatchStat teamMatchStat : teamMatchStats) {
                if (teamMatchStat.getTeamId() == team.getId()) {
                    // Счётчик сыгранных игр в групповом этапе
                    int countMatch = 0;

                    // Проходимся по всех матчам группового этапа, чтобы собрать статистику команды за группу
                    for (GroupStageMatch groupStageMatch : groupStageMatches) {
                        if (groupStageMatch.getMatch().getId() == teamMatchStat.getMatchId() && groupStageMatch.getGroupStage().getTournament().getId() == tournamentId) {
                            // Записываем к какой группе принадлежит команда
                            teamInfo.setGroup(groupStageService.getGroupStageById(groupStageMatch.getGroupStage().getId()));

                            if (!groupStageMatch.getMatch().getDate().equals("1970-01-01")) {
                                // Находим противника в текущем матче, для сравнения статистик
                                boolean isFind = false;
                                for (Team team1 : teams) {
                                    for (TeamMatchStat teamMatchStat1 : teamMatchStats) {
                                        if (team1.getId() == teamMatchStat1.getTeamId() && team1.getId() != team.getId()) {
                                            for (GroupStageMatch groupStageMatch1 : groupStageMatches) {
                                                if (groupStageMatch1.getMatch().getId() == groupStageMatch.getMatch().getId() && teamMatchStat1.getMatchId() == groupStageMatch1.getMatch().getId()) {
                                                    // Получаем статистику команд
                                                    teamInfo.groupStats.games++; // +1 игра для команды
                                                    teamInfo.groupStats.diffGoals += (teamMatchStat.getGoals() - teamMatchStat1.getGoals());
                                                    if (teamMatchStat.getGoals() > teamMatchStat1.getGoals()) {
                                                        teamInfo.groupStats.won++; // +1 победа для команды
                                                        teamInfo.groupStats.points += 3; // +3 очка для команды
                                                    } else if (teamMatchStat.getGoals() == teamMatchStat1.getGoals()) {
                                                        teamInfo.groupStats.draw++; // +1 ничья для команды
                                                        teamInfo.groupStats.points += 1; // +1 очко для команды
                                                    } else if (teamMatchStat.getGoals() < teamMatchStat1.getGoals()) {
                                                        teamInfo.groupStats.loss++; // +1 поражение для команды
                                                        teamInfo.groupStats.points += 0;// +0 очко для команды
                                                    }
                                                    teamInfo.group = groupStageMatch.getGroupStage();
                                                    countMatch++;
                                                    isFind = true;
                                                }
                                                if (isFind) break;
                                            }
                                        }
                                        if (isFind) break;
                                    }
                                    if (isFind) break;
                                }
                            }
                            temp = teamInfo;
                        }
                    }
                }
            }
            if (temp.getGroup() !=  null) {
                teamInfos.add(teamInfo);
            }
        }
        return sortTeamInfos(teamInfos);
    }

    public List<TeamInfo> getTeamInfosByGroupAndTournament(int tournamentId, int groupId) {
        List<TeamInfo> teamInfos = new ArrayList<>();
        for (TeamInfo teamInfo : getTeamInfosByTournamentId(tournamentId)) {
            if (teamInfo.getGroup().getId() == groupId) {
                teamInfos.add(teamInfo);
            }
        }
        return sortTeamInfos(teamInfos);
    }

    public TeamMatchStat doesTeamPlayedInMatchById(int teamId, int matchId) {
        for (TeamMatchStat teamMatchStat : teamMatchStatRepository.findAll()) {
            if (teamMatchStat.getMatchId() == matchId && teamMatchStat.getTeamId() == teamId) {
                return teamMatchStat;
            }
        }
        return null;
    }

    public List<Team> getTeamsByTournamentAndPlayOffStage(int tournamentId, int playOffStageId) {
        List<PlayOffStage> playOffStages = playOffStageRepository.findAll();
        List<TeamMatchStat> teamMatchStats = teamMatchStatRepository.findAll();
        List<PlayOffMatch> playOffMatches = playOffMatchRepository.findAll();
        List<TeamInfo> tournamentTeams = getTeamInfosByTournamentId(tournamentId);
        List<Team> teams = new ArrayList<>();

        for (PlayOffStage playOffStage : playOffStages) {
            if (playOffStage.getId() == playOffStageId && playOffStage.getPlayOff().getTournament().getId() == tournamentId) {
                switch (playOffStage.getName()) {
                    case "1/8 Финала":
                        for (TeamInfo teamInfo : getTeamInfosByTournamentId(tournamentId)) {
                            if (teamInfo.getGroupStats().getQualifiedStatus() == "QUALIFIED" && teamInfo.getGroup().getTournament().getId() == tournamentId) {
                                teams.add(teamInfo.getTeam());
                            }
                        }
                        break;
                    case "1/4 Финала":
                        PlayOffStage required = playOffStageService.getPlayOffStagesByName("1/8 Финала");
                        for (PlayOffMatch playOffMatch : playOffMatchService.getPlayOffMatchesByTournamentId(tournamentId)) {
                            if (required.getId() == playOffMatch.getPlayOffStageId() && playOffMatch.getTour() == 1) {
                                for (TeamInfo teamInfo : tournamentTeams) {
                                    if (teamInfo.getPlayOff().getTournament().getId() == tournamentId) {
                                        for (TeamMatchStat teamMatchStat : teamMatchStats) {
                                            boolean isFind = false;
                                            if (teamMatchStat.getMatchId() == playOffMatch.getMatch().getId() && teamInfo.getTeam().getId() == teamMatchStat.getTeamId()) {
                                                for (TeamMatchStat teamMatchStat1 : teamMatchStats) {
                                                    if (teamMatchStat1.getMatchId() == playOffMatch.getMatch().getId() && teamMatchStat1.getTeamId() != teamInfo.getTeam().getId()) {
                                                        // Команда победила в 1 туре
                                                        if (teamMatchStat.getGoals() > teamMatchStat1.getGoals()) {
                                                            // Ищем матч второго тура
                                                            for (PlayOffMatch playOffMatch2 : playOffMatches) {
                                                                if (playOffMatch2.getTour() == 2 && required.getId() == playOffMatch.getPlayOffStageId()) {
                                                                    // Играла ли команда в матче второго тура
                                                                    TeamMatchStat teamMatchStatTourTwo1 = doesTeamPlayedInMatchById(teamMatchStat.getTeamId(), playOffMatch2.getMatch().getId());
                                                                    TeamMatchStat teamMatchStatTourTwo2 = doesTeamPlayedInMatchById(teamMatchStat1.getTeamId(), playOffMatch2.getMatch().getId());

                                                                    if (teamMatchStatTourTwo1 != null && teamMatchStatTourTwo2 != null) {
                                                                        if (teamMatchStatTourTwo1.getGoals() > teamMatchStatTourTwo2.getGoals() || teamMatchStatTourTwo1.getGoals() == teamMatchStatTourTwo2.getGoals()) {
                                                                            teams.add(teamInfo.getTeam());
                                                                            isFind = true;
                                                                            break;
                                                                        } else if (teamMatchStat.getGoals() + teamMatchStatTourTwo1.getGoals() > teamMatchStat1.getGoals() + teamMatchStatTourTwo2.getGoals()){
                                                                            teams.add(teamInfo.getTeam());
                                                                            isFind = true;
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        // Команда сыграла вничью в 1 туре
                                                        else if (teamMatchStat.getGoals() == teamMatchStat1.getGoals()) {
                                                            // Ищем матч второго тура
                                                            for (PlayOffMatch playOffMatch2 : playOffMatches) {
                                                                if (playOffMatch2.getTour() == 2 && required.getId() == playOffMatch.getPlayOffStageId()) {
                                                                    // Играла ли команда в матче второго тура
                                                                    TeamMatchStat teamMatchStatTourTwo1 = doesTeamPlayedInMatchById(teamMatchStat.getTeamId(), playOffMatch2.getMatch().getId());
                                                                    TeamMatchStat teamMatchStatTourTwo2 = doesTeamPlayedInMatchById(teamMatchStat1.getTeamId(), playOffMatch2.getMatch().getId());

                                                                    if (teamMatchStatTourTwo1 != null && teamMatchStatTourTwo2 != null) {
                                                                        if (teamMatchStatTourTwo1.getGoals() > teamMatchStatTourTwo2.getGoals()) {
                                                                            teams.add(teamInfo.getTeam());
                                                                            isFind = true;
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        // Команда сыграла проиграла в 1 туре
                                                        else if (teamMatchStat.getGoals() < teamMatchStat1.getGoals()) {
                                                            // Ищем матч второго тура
                                                            for (PlayOffMatch playOffMatch2 : playOffMatches) {
                                                                if (playOffMatch2.getTour() == 2 && required.getId() == playOffMatch.getPlayOffStageId()) {
                                                                    // Играла ли команда в матче второго тура
                                                                    TeamMatchStat teamMatchStatTourTwo1 = doesTeamPlayedInMatchById(teamMatchStat.getTeamId(), playOffMatch2.getMatch().getId());
                                                                    TeamMatchStat teamMatchStatTourTwo2 = doesTeamPlayedInMatchById(teamMatchStat1.getTeamId(), playOffMatch2.getMatch().getId());

                                                                    if (teamMatchStatTourTwo1 != null && teamMatchStatTourTwo2 != null) {
                                                                        if (teamMatchStatTourTwo1.getGoals() > teamMatchStatTourTwo2.getGoals()) {
                                                                            if (teamMatchStat1.getGoals() - teamMatchStat.getGoals() < teamMatchStatTourTwo1.getGoals() - teamMatchStatTourTwo2.getGoals()) {
                                                                                teams.add(teamInfo.getTeam());
                                                                                isFind = true;
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if (isFind) break;
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case "Полуфинал":
                        PlayOffStage required1 = playOffStageService.getPlayOffStagesByName("1/4 Финала");
                        for (PlayOffMatch playOffMatch : playOffMatchService.getPlayOffMatchesByTournamentId(tournamentId)) {
                            if (required1.getId() == playOffMatch.getPlayOffStageId() && playOffMatch.getTour() == 1) {
                                for (TeamInfo teamInfo : tournamentTeams) {
                                    if (teamInfo.getPlayOff().getTournament().getId() == tournamentId) {
                                        for (TeamMatchStat teamMatchStat : teamMatchStats) {
                                            boolean isFind = false;
                                            if (teamMatchStat.getMatchId() == playOffMatch.getMatch().getId() && teamInfo.getTeam().getId() == teamMatchStat.getTeamId()) {
                                                for (TeamMatchStat teamMatchStat1 : teamMatchStats) {
                                                    if (teamMatchStat1.getMatchId() == playOffMatch.getMatch().getId() && teamMatchStat1.getTeamId() != teamInfo.getTeam().getId()) {
                                                        // Команда победила в 1 туре
                                                        if (teamMatchStat.getGoals() > teamMatchStat1.getGoals()) {
                                                            // Ищем матч второго тура
                                                            for (PlayOffMatch playOffMatch2 : playOffMatches) {
                                                                if (playOffMatch2.getTour() == 2 && required1.getId() == playOffMatch.getPlayOffStageId()) {
                                                                    // Играла ли команда в матче второго тура
                                                                    TeamMatchStat teamMatchStatTourTwo1 = doesTeamPlayedInMatchById(teamMatchStat.getTeamId(), playOffMatch2.getMatch().getId());
                                                                    TeamMatchStat teamMatchStatTourTwo2 = doesTeamPlayedInMatchById(teamMatchStat1.getTeamId(), playOffMatch2.getMatch().getId());

                                                                    if (teamMatchStatTourTwo1 != null && teamMatchStatTourTwo2 != null) {
                                                                        if (teamMatchStatTourTwo1.getGoals() > teamMatchStatTourTwo2.getGoals() || teamMatchStatTourTwo1.getGoals() == teamMatchStatTourTwo2.getGoals()) {
                                                                            teams.add(teamInfo.getTeam());
                                                                            isFind = true;
                                                                            break;
                                                                        } else if (teamMatchStat.getGoals() + teamMatchStatTourTwo1.getGoals() > teamMatchStat1.getGoals() + teamMatchStatTourTwo2.getGoals()) {
                                                                            teams.add(teamInfo.getTeam());
                                                                            isFind = true;
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        // Команда сыграла вничью в 1 туре
                                                        else if (teamMatchStat.getGoals() == teamMatchStat1.getGoals()) {
                                                            // Ищем матч второго тура
                                                            for (PlayOffMatch playOffMatch2 : playOffMatches) {
                                                                if (playOffMatch2.getTour() == 2 && required1.getId() == playOffMatch.getPlayOffStageId()) {
                                                                    // Играла ли команда в матче второго тура
                                                                    TeamMatchStat teamMatchStatTourTwo1 = doesTeamPlayedInMatchById(teamMatchStat.getTeamId(), playOffMatch2.getMatch().getId());
                                                                    TeamMatchStat teamMatchStatTourTwo2 = doesTeamPlayedInMatchById(teamMatchStat1.getTeamId(), playOffMatch2.getMatch().getId());

                                                                    if (teamMatchStatTourTwo1 != null && teamMatchStatTourTwo2 != null) {
                                                                        if (teamMatchStatTourTwo1.getGoals() > teamMatchStatTourTwo2.getGoals()) {
                                                                            teams.add(teamInfo.getTeam());
                                                                            isFind = true;
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        // Команда сыграла проиграла в 1 туре
                                                        else if (teamMatchStat.getGoals() < teamMatchStat1.getGoals()) {
                                                            // Ищем матч второго тура
                                                            for (PlayOffMatch playOffMatch2 : playOffMatches) {
                                                                if (playOffMatch2.getTour() == 2 && required1.getId() == playOffMatch.getPlayOffStageId()) {
                                                                    // Играла ли команда в матче второго тура
                                                                    TeamMatchStat teamMatchStatTourTwo1 = doesTeamPlayedInMatchById(teamMatchStat.getTeamId(), playOffMatch2.getMatch().getId());
                                                                    TeamMatchStat teamMatchStatTourTwo2 = doesTeamPlayedInMatchById(teamMatchStat1.getTeamId(), playOffMatch2.getMatch().getId());

                                                                    if (teamMatchStatTourTwo1 != null && teamMatchStatTourTwo2 != null) {
                                                                        if (teamMatchStatTourTwo1.getGoals() > teamMatchStatTourTwo2.getGoals()) {
                                                                            if (teamMatchStat.getGoals() + teamMatchStatTourTwo1.getGoals() > teamMatchStat1.getGoals() + teamMatchStatTourTwo2.getGoals()) {
                                                                                teams.add(teamInfo.getTeam());
                                                                                isFind = true;
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if (isFind) break;
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case "Финал":
                        PlayOffStage required2 = playOffStageService.getPlayOffStagesByName("Полуфинал");
                        for (PlayOffMatch playOffMatch : playOffMatchService.getPlayOffMatchesByTournamentId(tournamentId)) {
                            if (required2.getId() == playOffMatch.getPlayOffStageId() && playOffMatch.getTour() == 1) {
                                for (TeamInfo teamInfo : tournamentTeams) {
                                    for (TeamMatchStat teamMatchStat : teamMatchStats) {
                                        boolean isFind = false;
                                        if (teamMatchStat.getMatchId() == playOffMatch.getMatch().getId() && teamInfo.getTeam().getId() == teamMatchStat.getTeamId()) {
                                            for (TeamMatchStat teamMatchStat1 : teamMatchStats) {
                                                if (teamMatchStat1.getMatchId() == playOffMatch.getMatch().getId() && teamMatchStat1.getTeamId() != teamInfo.getTeam().getId()) {
                                                    // Команда победила в 1 туре
                                                    if (teamMatchStat.getGoals() > teamMatchStat1.getGoals()) {
                                                        // Ищем матч второго тура
                                                        for (PlayOffMatch playOffMatch2 : playOffMatches) {
                                                            if (playOffMatch2.getTour() == 2 && required2.getId() == playOffMatch.getPlayOffStageId()) {
                                                                // Играла ли команда в матче второго тура
                                                                TeamMatchStat teamMatchStatTourTwo1 = doesTeamPlayedInMatchById(teamInfo.getTeam().getId(), playOffMatch2.getMatch().getId());
                                                                TeamMatchStat teamMatchStatTourTwo2 = doesTeamPlayedInMatchById(teamMatchStat1.getTeamId(), playOffMatch2.getMatch().getId());

                                                                if (teamMatchStatTourTwo1 != null && teamMatchStatTourTwo2 != null) {
                                                                    if (teamMatchStatTourTwo1.getGoals() > teamMatchStatTourTwo2.getGoals() || teamMatchStatTourTwo1.getGoals() == teamMatchStatTourTwo2.getGoals()) {
                                                                        teams.add(teamInfo.getTeam());
                                                                        isFind = true;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    // Команда сыграла вничью в 1 туре
                                                    else if (teamMatchStat.getGoals() == teamMatchStat1.getGoals()) {
                                                        // Ищем матч второго тура
                                                        for (PlayOffMatch playOffMatch2 : playOffMatches) {
                                                            if (playOffMatch2.getTour() == 2 && required2.getId() == playOffMatch.getPlayOffStageId()) {
                                                                // Играла ли команда в матче второго тура
                                                                TeamMatchStat teamMatchStatTourTwo1 = doesTeamPlayedInMatchById(teamInfo.getTeam().getId(), playOffMatch2.getMatch().getId());
                                                                TeamMatchStat teamMatchStatTourTwo2 = doesTeamPlayedInMatchById(teamMatchStat1.getTeamId(), playOffMatch2.getMatch().getId());

                                                                if (teamMatchStatTourTwo1 != null && teamMatchStatTourTwo2 != null) {
                                                                    if (teamMatchStatTourTwo1.getGoals() > teamMatchStatTourTwo2.getGoals()) {
                                                                        teams.add(teamInfo.getTeam());
                                                                        isFind = true;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    // Команда сыграла проиграла в 1 туре
                                                    else if (teamMatchStat.getGoals() < teamMatchStat1.getGoals()) {
                                                        // Ищем матч второго тура
                                                        for (PlayOffMatch playOffMatch2 : playOffMatches) {
                                                            if (playOffMatch2.getTour() == 2 && required2.getId() == playOffMatch.getPlayOffStageId()) {
                                                                // Играла ли команда в матче второго тура
                                                                TeamMatchStat teamMatchStatTourTwo1 = doesTeamPlayedInMatchById(teamInfo.getTeam().getId(), playOffMatch2.getMatch().getId());
                                                                TeamMatchStat teamMatchStatTourTwo2 = doesTeamPlayedInMatchById(teamMatchStat1.getTeamId(), playOffMatch2.getMatch().getId());

                                                                if (teamMatchStatTourTwo1 != null && teamMatchStatTourTwo2 != null) {
                                                                    if (teamMatchStatTourTwo1.getGoals() > teamMatchStatTourTwo2.getGoals()) {
                                                                        if (teamMatchStat1.getGoals() - teamMatchStat.getGoals() < teamMatchStatTourTwo1.getGoals() - teamMatchStatTourTwo2.getGoals()) {
                                                                            teams.add(teamInfo.getTeam());
                                                                            isFind = true;
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (isFind) break;
                                    }
                                }
                            }
                        }
                        break;
                }
                break;
            }
        }
        return teams;
    }
}
