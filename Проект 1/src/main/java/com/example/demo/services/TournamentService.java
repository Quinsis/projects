package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.repositories.TeamMatchStatRepository;
import com.example.demo.repositories.TournamentRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

    @RequiredArgsConstructor
    @Slf4j
    @Service
public class TournamentService {
    @Data
    public class Winner {
        public Team team;
        public Tournament tournament;
    }
    private final TournamentRepository tournamentRepository;
    private final PlayOffMatchService playOffMatchService;
    private final PlayOffStageService playOffStageService;
    private final TeamMatchStatService teamMatchStatService;
    private final TeamService teamService;
    public List<Tournament> getTournaments() {
        return this.tournamentRepository.findAll();
    }

    public Tournament getTournamentById(int id) {
        for (Tournament tournament : tournamentRepository.findAll()) {
            if (tournament.getId() == id) {
                return tournament;
            }
        }
        return null;
    }

    public List<Winner> getWinnersOfTournaments() {
        List<Winner> winners = new ArrayList<>();
        for (Tournament tournament : tournamentRepository.findAll()) {
            for (PlayOffMatch playOffMatch : playOffMatchService.getPlayOffMatchesByTournamentId(tournament.getId())) {
                if (playOffStageService.getNameOfPlayOffStageById(playOffMatch.getPlayOffStageId()).equals("Финал")) {
                    Winner winner = new Winner();
                    winner.tournament = tournament;
                    List<TeamMatchStat> stats = teamMatchStatService.getTeamMatchStatsByMatchId(playOffMatch.getMatch().getId());
                    if (stats.get(0).getGoals() > stats.get(1).getGoals()) {
                        winner.team = teamService.getTeamById(stats.get(0).getTeamId());
                        winners.add(winner);
                    } else if (stats.get(0).getGoals() < stats.get(1).getGoals()){
                        winner.team = teamService.getTeamById(stats.get(1).getTeamId());
                        winners.add(winner);
                    }
                }
            }
        }
        return winners;
    }

    public void createTournament(Tournament tournament) {
        this.tournamentRepository.save(tournament);
    }
}
