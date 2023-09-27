package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OPERATOR')")
public class MatchController {
    private final GroupStageMatchService groupStageMatchService;
    private final UserService userService;
    private final GroupStageService groupStageService;
    private final TournamentService tournamentService;
    private final PlayOffStageService playOffStageService;
    private final TeamMatchStatService teamMatchStatService;
    private final TeamService teamService;
    private final MatchService matchService;
    private final PlayOffMatchService playOffMatchService;

    @GetMapping("/tournament/{tournamentId}/group/{groupId}/match/{id}/edit")
    public ModelAndView edit(@PathVariable int id,
                                  @PathVariable int tournamentId,
                                  @PathVariable int groupId,
                                  Model match,
                                  Model user,
                                  Model group,
                                  Model tournament,
                                  Principal principal) {
        group.addAttribute("group", groupStageService.getGroupStageById(groupId));
        tournament.addAttribute("tournament", tournamentService.getTournamentById(tournamentId));
        user.addAttribute("user", userService.getUserByName(principal.getName()));
        match.addAttribute("match", groupStageMatchService.getMatchInfoById(id));
        return new ModelAndView("matchEdit");
    }

    @GetMapping("/tournament/{tournamentId}/playOff/{playOffStageId}/match/{id}/edit")
    public ModelAndView playOffMatchEdit(@PathVariable int id,
                             @PathVariable int tournamentId,
                             @PathVariable int playOffStageId,
                             Model match,
                             Model user,
                             Model tournament,
                             Model playOffStage,
                             Principal principal) {
        tournament.addAttribute("tournament", tournamentService.getTournamentById(tournamentId));
        playOffStage.addAttribute("playOffStage", playOffStageService.getPlayOffStagesById(playOffStageId));
        user.addAttribute("user", userService.getUserByName(principal.getName()));
        match.addAttribute("match", playOffMatchService.getMatchInfoById(id));
        return new ModelAndView("playOffMatchEdit");
    }

    @PostMapping("/edit/{ownerStat}/{guestStat}")
    public ModelAndView edit(@RequestParam ("totalAttempts") int totalAttempts,
                             @RequestParam ("goalAttempts") int goalAttempts,
                             @RequestParam ("possession") int possession,
                             @RequestParam ("passes") int passes,
                             @RequestParam ("passesAccuracy") int passesAccuracy,
                             @RequestParam ("fouls") int fouls,
                             @RequestParam ("yellowCards") int yellowCards,
                             @RequestParam ("redCards") int redCards,
                             @RequestParam ("offsides") int offsides,
                             @RequestParam ("corners") int corners,
                             @RequestParam ("totalAttempts1") int totalAttempts1,
                             @RequestParam ("goalAttempts1") int goalAttempts1,
                             @RequestParam ("possession1") int possession1,
                             @RequestParam ("passes1") int passes1,
                             @RequestParam ("passesAccuracy1") int passesAccuracy1,
                             @RequestParam ("fouls1") int fouls1,
                             @RequestParam ("yellowCards1") int yellowCards1,
                             @RequestParam ("redCards1") int redCards1,
                             @RequestParam ("offsides1") int offsides1,
                             @RequestParam ("corners1") int corners1,
                             @RequestParam ("goals") int goals,
                             @RequestParam ("goals1") int goals1,
                             @PathVariable int ownerStat,
                             @PathVariable int guestStat) {
        teamMatchStatService.getStatById(ownerStat).setGoals(goals);
        teamMatchStatService.getStatById(ownerStat).setTotalAttempts(totalAttempts);
        teamMatchStatService.getStatById(ownerStat).setGoalAttempts(goalAttempts);
        teamMatchStatService.getStatById(ownerStat).setPossession(possession);
        teamMatchStatService.getStatById(ownerStat).setPasses(passes);
        teamMatchStatService.getStatById(ownerStat).setPassesAccuracy(passesAccuracy);
        teamMatchStatService.getStatById(ownerStat).setFouls(fouls);
        teamMatchStatService.getStatById(ownerStat).setYellowCards(yellowCards);
        teamMatchStatService.getStatById(ownerStat).setRedCards(redCards);
        teamMatchStatService.getStatById(ownerStat).setOffsides(offsides);
        teamMatchStatService.getStatById(ownerStat).setCorners(corners);

        teamMatchStatService.getStatById(guestStat).setGoals(goals1);
        teamMatchStatService.getStatById(guestStat).setTotalAttempts(totalAttempts1);
        teamMatchStatService.getStatById(guestStat).setGoalAttempts(goalAttempts1);
        teamMatchStatService.getStatById(guestStat).setPossession(possession1);
        teamMatchStatService.getStatById(guestStat).setPasses(passes1);
        teamMatchStatService.getStatById(guestStat).setPassesAccuracy(passesAccuracy1);
        teamMatchStatService.getStatById(guestStat).setFouls(fouls1);
        teamMatchStatService.getStatById(guestStat).setYellowCards(yellowCards1);
        teamMatchStatService.getStatById(guestStat).setRedCards(redCards1);
        teamMatchStatService.getStatById(guestStat).setOffsides(offsides1);
        teamMatchStatService.getStatById(guestStat).setCorners(corners1);

        teamMatchStatService.changeStat(teamMatchStatService.getStatById(ownerStat));
        teamMatchStatService.changeStat(teamMatchStatService.getStatById(guestStat));

        GroupStage groupStage = this.groupStageMatchService.getGroupStageByMatchId(teamMatchStatService.getStatById(ownerStat).getMatchId());

        return new ModelAndView("redirect:/tournament/" + groupStage.getTournament().getId() + "/group/" + groupStage.getId());
    }

    @PostMapping("/tournament/{tournamentId}/playOff/{playOffStageId}/match/{matchId}/edit/{ownerStat}/{guestStat}")
    public ModelAndView playOffMatchEdit(@RequestParam ("totalAttempts") int totalAttempts,
                             @RequestParam ("goalAttempts") int goalAttempts,
                             @RequestParam ("possession") int possession,
                             @RequestParam ("passes") int passes,
                             @RequestParam ("passesAccuracy") int passesAccuracy,
                             @RequestParam ("fouls") int fouls,
                             @RequestParam ("yellowCards") int yellowCards,
                             @RequestParam ("redCards") int redCards,
                             @RequestParam ("offsides") int offsides,
                             @RequestParam ("corners") int corners,
                             @RequestParam ("totalAttempts1") int totalAttempts1,
                             @RequestParam ("goalAttempts1") int goalAttempts1,
                             @RequestParam ("possession1") int possession1,
                             @RequestParam ("passes1") int passes1,
                             @RequestParam ("passesAccuracy1") int passesAccuracy1,
                             @RequestParam ("fouls1") int fouls1,
                             @RequestParam ("yellowCards1") int yellowCards1,
                             @RequestParam ("redCards1") int redCards1,
                             @RequestParam ("offsides1") int offsides1,
                             @RequestParam ("corners1") int corners1,
                             @RequestParam ("goals") int goals,
                             @RequestParam ("goals1") int goals1,
                             @PathVariable int ownerStat,
                             @PathVariable int guestStat,
                                         @PathVariable int tournamentId) {

        teamMatchStatService.getStatById(ownerStat).setGoals(goals);
        teamMatchStatService.getStatById(ownerStat).setTotalAttempts(totalAttempts);
        teamMatchStatService.getStatById(ownerStat).setGoalAttempts(goalAttempts);
        teamMatchStatService.getStatById(ownerStat).setPossession(possession);
        teamMatchStatService.getStatById(ownerStat).setPasses(passes);
        teamMatchStatService.getStatById(ownerStat).setPassesAccuracy(passesAccuracy);
        teamMatchStatService.getStatById(ownerStat).setFouls(fouls);
        teamMatchStatService.getStatById(ownerStat).setYellowCards(yellowCards);
        teamMatchStatService.getStatById(ownerStat).setRedCards(redCards);
        teamMatchStatService.getStatById(ownerStat).setOffsides(offsides);
        teamMatchStatService.getStatById(ownerStat).setCorners(corners);

        teamMatchStatService.getStatById(guestStat).setGoals(goals1);
        teamMatchStatService.getStatById(guestStat).setTotalAttempts(totalAttempts1);
        teamMatchStatService.getStatById(guestStat).setGoalAttempts(goalAttempts1);
        teamMatchStatService.getStatById(guestStat).setPossession(possession1);
        teamMatchStatService.getStatById(guestStat).setPasses(passes1);
        teamMatchStatService.getStatById(guestStat).setPassesAccuracy(passesAccuracy1);
        teamMatchStatService.getStatById(guestStat).setFouls(fouls1);
        teamMatchStatService.getStatById(guestStat).setYellowCards(yellowCards1);
        teamMatchStatService.getStatById(guestStat).setRedCards(redCards1);
        teamMatchStatService.getStatById(guestStat).setOffsides(offsides1);
        teamMatchStatService.getStatById(guestStat).setCorners(corners1);

        teamMatchStatService.changeStat(teamMatchStatService.getStatById(ownerStat));
        teamMatchStatService.changeStat(teamMatchStatService.getStatById(guestStat));

        return new ModelAndView("redirect:/tournament/" + tournamentId);
    }

    @GetMapping("/tournament/{tournamentId}/group/{groupId}/createMatch")
    public ModelAndView create(@PathVariable int tournamentId,
                               @PathVariable int groupId,
                               Principal principal,
                               Model teams,
                               Model user,
                               Model group,
                               Model tournament,
                               Model error) {
        error.addAttribute("errorMessage", null);
        user.addAttribute("user", userService.getUserByName(principal.getName()));
        teams.addAttribute("teams", teamService.getTeamInfosByGroupAndTournament(tournamentId, groupId));
        group.addAttribute("group", groupStageService.getGroupStageById(groupId));
        tournament.addAttribute("tournament", tournamentService.getTournamentById(tournamentId));
        return new ModelAndView("createMatch");
    }

    @GetMapping("/tournament/{tournamentId}/playOff/{playOffStageId}/createPlayOffMatch/tour/{tour}")
    public ModelAndView createPlayOffMatch(@PathVariable int tournamentId,
                               @PathVariable int playOffStageId,
                               @PathVariable int tour,
                               Principal principal,
                               Model teams,
                               Model user,
                               Model tournament) {
        user.addAttribute("user", userService.getUserByName(principal.getName()));
        teams.addAttribute("teams", teamService.getTeamsByTournamentAndPlayOffStage(tournamentId, playOffStageId));
        tournament.addAttribute("tournament", tournamentService.getTournamentById(tournamentId));
        return new ModelAndView("createPlayOffMatch");
    }



    @PostMapping("/group/{groupId}/createMatch")
    public ModelAndView create(@PathVariable int groupId,
                               @RequestParam ("totalAttempts") int totalAttempts,
                               @RequestParam ("goalAttempts") int goalAttempts,
                               @RequestParam ("possession") int possession,
                               @RequestParam ("passes") int passes,
                               @RequestParam ("passesAccuracy") int passesAccuracy,
                               @RequestParam ("fouls") int fouls,
                               @RequestParam ("yellowCards") int yellowCards,
                               @RequestParam ("redCards") int redCards,
                               @RequestParam ("offsides") int offsides,
                               @RequestParam ("corners") int corners,
                               @RequestParam ("totalAttempts1") int totalAttempts1,
                               @RequestParam ("goalAttempts1") int goalAttempts1,
                               @RequestParam ("possession1") int possession1,
                               @RequestParam ("passes1") int passes1,
                               @RequestParam ("passesAccuracy1") int passesAccuracy1,
                               @RequestParam ("fouls1") int fouls1,
                               @RequestParam ("yellowCards1") int yellowCards1,
                               @RequestParam ("redCards1") int redCards1,
                               @RequestParam ("offsides1") int offsides1,
                               @RequestParam ("corners1") int corners1,
                               @RequestParam ("goals") int goals,
                               @RequestParam ("goals1") int goals1,
                               @RequestParam ("matchDate") String matchDate,
                               @RequestParam ("teamOwnerSelect") String teamOwnerName,
                               @RequestParam ("teamGuestSelect") String teamGuestName) {
        if (!teamOwnerName.equals(teamGuestName)) {
            matchService.addMatch(new Match(matchDate));
            groupStageMatchService.addGroupStageMatch(new GroupStageMatch(groupStageService.getGroupStageById(groupId), matchService.getMatches().get(matchService.getMatches().size() - 1)));
            teamMatchStatService.addStat(new TeamMatchStat(teamService.getTeamByName(teamOwnerName).getId(),
                    matchService.getMatches().get(matchService.getMatches().size() - 1).getId(),
                    goals, totalAttempts, goalAttempts, possession, passes, passesAccuracy, fouls, yellowCards, redCards, offsides, corners));
            teamMatchStatService.addStat(new TeamMatchStat(teamService.getTeamByName(teamGuestName).getId(),
                    matchService.getMatches().get(matchService.getMatches().size() - 1).getId(),
                    goals1, totalAttempts1, goalAttempts1, possession1, passes1, passesAccuracy1, fouls1, yellowCards1, redCards1, offsides1, corners1));
        }
        return new ModelAndView("redirect:/tournament/" + groupStageService.getGroupStageById(groupId).getTournament().getId() + "/group/" + groupId);
    }

    @PostMapping("/tournament/{tournamentId}/playOff/{playOffStageId}/createPlayOffMatch/tour/{tour}")
    public ModelAndView createPlayOffMatch(@PathVariable int tournamentId,
                                           @PathVariable int playOffStageId,
                                           @PathVariable int tour,
                               @RequestParam ("totalAttempts") int totalAttempts,
                               @RequestParam ("goalAttempts") int goalAttempts,
                               @RequestParam ("possession") int possession,
                               @RequestParam ("passes") int passes,
                               @RequestParam ("passesAccuracy") int passesAccuracy,
                               @RequestParam ("fouls") int fouls,
                               @RequestParam ("yellowCards") int yellowCards,
                               @RequestParam ("redCards") int redCards,
                               @RequestParam ("offsides") int offsides,
                               @RequestParam ("corners") int corners,
                               @RequestParam ("totalAttempts1") int totalAttempts1,
                               @RequestParam ("goalAttempts1") int goalAttempts1,
                               @RequestParam ("possession1") int possession1,
                               @RequestParam ("passes1") int passes1,
                               @RequestParam ("passesAccuracy1") int passesAccuracy1,
                               @RequestParam ("fouls1") int fouls1,
                               @RequestParam ("yellowCards1") int yellowCards1,
                               @RequestParam ("redCards1") int redCards1,
                               @RequestParam ("offsides1") int offsides1,
                               @RequestParam ("corners1") int corners1,
                               @RequestParam ("goals") int goals,
                               @RequestParam ("goals1") int goals1,
                               @RequestParam ("matchDate") String matchDate,
                               @RequestParam ("teamOwnerSelect") String teamOwnerName,
                               @RequestParam ("teamGuestSelect") String teamGuestName) {
        if (!teamOwnerName.equals(teamGuestName)) {
            matchService.addMatch(new Match(matchDate));
            playOffMatchService.addPlayOffMatch(new PlayOffMatch(playOffStageId, matchService.getMatches().get(matchService.getMatches().size() - 1), tour));
            teamMatchStatService.addStat(new TeamMatchStat(teamService.getTeamByName(teamOwnerName).getId(),
                    matchService.getMatches().get(matchService.getMatches().size() - 1).getId(),
                    goals, totalAttempts, goalAttempts, possession, passes, passesAccuracy, fouls, yellowCards, redCards, offsides, corners));
            teamMatchStatService.addStat(new TeamMatchStat(teamService.getTeamByName(teamGuestName).getId(),
                    matchService.getMatches().get(matchService.getMatches().size() - 1).getId(),
                    goals1, totalAttempts1, goalAttempts1, possession1, passes1, passesAccuracy1, fouls1, yellowCards1, redCards1, offsides1, corners1));
        }
        return new ModelAndView("redirect:/tournament/" + tournamentId);
    }
}
