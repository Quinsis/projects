package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TournamentController {
    private final TournamentService tournamentService;
    private final GroupStageService groupStageService;
    private final TeamService teamService;
    private final UserService userService;
    private final PlayOffStageService playOffStageService;
    private final PlayOffMatchService playOffMatchService;
    private final PlayOffService playOffService;

    @GetMapping("/")
    public ModelAndView tournament(Model model, Model user, Principal principal, Model winners) {
        user.addAttribute("user", userService.getUserByName(principal.getName()));
        model.addAttribute("tournaments", tournamentService.getTournaments());
        winners.addAttribute("winners", tournamentService.getWinnersOfTournaments());
        return new ModelAndView("index");
    }

    @GetMapping("/tournament/{id}")
    public ModelAndView tournament(@PathVariable int id,
                                   Model user,
                                   Principal principal,
                                   Model groups,
                                   Model teamsInfo,
                                   Model tournament,
                                   Model playOffStages,
                                   Model playOffMatchesTourOne,
                                   Model playOffMatchesTourTwo,
                                   Model oneOfEighthMatchesTourOne,
                                   Model oneOfFourthMatchesTourOne,
                                   Model semiFinalMatchesTourOne,
                                   Model oneOfEighthMatchesTourTwo,
                                   Model oneOfFourthMatchesTourTwo,
                                   Model semiFinalMatchesTourTwo,
                                   Model finalMatches,
                                   Model currentPlayOffTours) {

        List<Integer> playOffTours = new ArrayList<>();
        playOffTours.add(1);
        playOffTours.add(1);
        playOffTours.add(1);
        playOffTours.add(1);

        user.addAttribute("user", userService.getUserByName(principal.getName()));
        groups.addAttribute("groups", groupStageService.getGroupStagesByTournamentId(id));
        teamsInfo.addAttribute("teamsInfo", teamService.getTeamInfosByTournamentId(id));
        tournament.addAttribute("tournament", tournamentService.getTournamentById(id));
        playOffStages.addAttribute("playOffStages", playOffStageService.getPlayOffStagesByTournamentId(id));
        playOffMatchesTourOne.addAttribute("playOffMatchesTourOne", playOffMatchService.getInfoOfMatchesByTournamentAndTour(id, 1));
        playOffMatchesTourTwo.addAttribute("playOffMatchesTourTwo", playOffMatchService.getInfoOfMatchesByTournamentAndTour(id, 2));
        currentPlayOffTours.addAttribute("currentPlayOffTours", playOffTours);

        oneOfEighthMatchesTourOne.addAttribute("oneOfEighthMatchesTourOne", playOffMatchService.getPlayOffMatchesByTournamentIdAndStageIdAndTour(id, "1/8 Финала", 1));
        oneOfFourthMatchesTourOne.addAttribute("oneOfFourthMatchesTourOne", playOffMatchService.getPlayOffMatchesByTournamentIdAndStageIdAndTour(id, "1/4 Финала", 1));
        semiFinalMatchesTourOne.addAttribute("semiFinalMatchesTourOne", playOffMatchService.getPlayOffMatchesByTournamentIdAndStageIdAndTour(id, "Полуфинал", 1));

        oneOfEighthMatchesTourTwo.addAttribute("oneOfEighthMatchesTourTwo", playOffMatchService.getPlayOffMatchesByTournamentIdAndStageIdAndTour(id, "1/8 Финала", 2));
        oneOfFourthMatchesTourTwo.addAttribute("oneOfFourthMatchesTourTwo", playOffMatchService.getPlayOffMatchesByTournamentIdAndStageIdAndTour(id, "1/4 Финала", 2));
        semiFinalMatchesTourTwo.addAttribute("semiFinalMatchesTourTwo", playOffMatchService.getPlayOffMatchesByTournamentIdAndStageIdAndTour(id, "Полуфинал", 2));

        finalMatches.addAttribute("finalMatches", playOffMatchService.getPlayOffMatchesByTournamentIdAndStageId(id, "Финал"));

        return new ModelAndView("tournament");
    }

    @GetMapping("/tournament/create")
    public ModelAndView create(Principal principal,
                               Model user) {
        user.addAttribute("user", userService.getUserByName(principal.getName()));
        return new ModelAndView("createTournament");
    }

    @PostMapping("/tournament/create")
    public ModelAndView create(@RequestParam("name") String name,
                               @RequestParam ("photo") String photo,
                               @RequestParam ("date_begin") String dateBegin,
                               @RequestParam ("date_end") String dateEnd) {
        Tournament tournament = new Tournament();
        tournament.setName(name);
        tournament.setPhoto("/static/img/tournaments/" + photo);
        tournament.setDate_begin(dateBegin);
        tournament.setDate_end(dateEnd);

        tournamentService.createTournament(tournament);

        List<GroupStage> groupStages = new ArrayList<>(List.of(
                new GroupStage[]{
                        new GroupStage("Группа A", tournament),
                        new GroupStage("Группа B", tournament),
                        new GroupStage("Группа C", tournament),
                        new GroupStage("Группа D", tournament),
                        new GroupStage("Группа E", tournament),
                        new GroupStage("Группа F", tournament),
                        new GroupStage("Группа G", tournament),
                        new GroupStage("Группа H", tournament)
                })
        );
        for (GroupStage groupStage : groupStages) {
            this.groupStageService.createGroupStage(groupStage);
        }

        PlayOff playOff = new PlayOff(tournament);
        playOffService.createPlayOff(playOff);


        List<PlayOffStage> playOffStages = new ArrayList<>(List.of(
                new PlayOffStage[]{
                        new PlayOffStage("1/8 Финала", playOff),
                        new PlayOffStage("1/4 Финала", playOff),
                        new PlayOffStage("Полуфинал", playOff),
                        new PlayOffStage("Финал", playOff)
                })
        );
        for (PlayOffStage playOffStage : playOffStages) {
            this.playOffStageService.createPlayOffStage(playOffStage);
        }

        return new ModelAndView("redirect:/");
    }
}
