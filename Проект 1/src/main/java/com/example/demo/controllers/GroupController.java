package com.example.demo.controllers;

import com.example.demo.models.GroupStageMatch;
import com.example.demo.models.Match;
import com.example.demo.models.Team;
import com.example.demo.models.TeamMatchStat;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GroupController {
    private final GroupStageService groupStageService;
    private final TeamService teamService;
    private final TournamentService tournamentService;
    private final GroupStageMatchService groupStageMatchService;
    private final UserService userService;
    private final MatchService matchService;
    private final TeamMatchStatService teamMatchStatService;

    @PostMapping("/tournament/{tournament}/group/{group}/addTeams")
    private ModelAndView addTeams(@PathVariable int tournament,
                                  @PathVariable int group,
                                  @RequestParam ("team[]") String[] teamNames) {
        for (Team team : teamService.getTeams()) {
            for (String teamName : teamNames) {
                if (team.getName().equals(teamName)) {
                    matchService.addMatch(new Match("1970-01-01"));
                    groupStageMatchService.addGroupStageMatch(new GroupStageMatch(groupStageService.getGroupStageById(group), matchService.getMatches().get(matchService.getMatches().size() - 1)));
                    teamMatchStatService.addStat(new TeamMatchStat(teamService.getTeamByName(teamName).getId(),
                            matchService.getMatches().get(matchService.getMatches().size() - 1).getId(),
                            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                    break;
                }
            }
        }
        return new ModelAndView("redirect:/tournament/" + tournament + "/group/" + group);
    }

    @GetMapping("/tournament/{tournamentId}/group/{groupId}")
    private ModelAndView groupInfo(Principal principal,
                                   Model user,
                                   @PathVariable int tournamentId,
                                   @PathVariable int groupId,
                                   Model teams,
                                   Model allTeams,
                                   Model teamSize,
                                   Model group,
                                   Model tournament,
                                   Model matches,
                                   Model flag) {
        List<TeamService.TeamInfo> teamsInGroup = teamService.getTeamInfosByGroupAndTournament(tournamentId, groupId);
        List<TeamService.TeamInfo> teamsInTournament = teamService.getTeamInfosByTournamentId(tournamentId);
        List<TeamService.TeamInfo> teamsAll = teamService.sortTeamsByName();
        flag.addAttribute("flag", false);

        for (int i = 0; i < teamsInTournament.size(); i++) {
            for (int j = 0; j < teamsAll.size(); j++) {
                if (teamsAll.get(j).getTeam().getId() == teamsInTournament.get(i).getTeam().getId()) {
                    teamsAll.remove(j);
                    break;
                }
            }
        }

        user.addAttribute("user", userService.getUserByName(principal.getName()));
        teams.addAttribute("teams", teamsInGroup);
        allTeams.addAttribute("allTeams", teamsAll);
        teamSize.addAttribute("teamSize", teamsInGroup.size());
        group.addAttribute("group", groupStageService.getGroupStageById(groupId));
        tournament.addAttribute("tournament", tournamentService.getTournamentById(tournamentId));
        matches.addAttribute("matches", groupStageMatchService.getInfoOfMatchesByGroupAndTournament(tournamentId, groupId));
        return new ModelAndView("group");
    }
}
