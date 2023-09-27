package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.services.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TeamController {
    private final UserService userService;
    private final PlayerService playerService;
    private final TeamService teamService;
    private final TeamMatchStatService teamMatchStatService;
    private final GroupStageMatchService groupStageMatchService;
    private final PlayOffMatchService playOffMatchService;

    @GetMapping("/team/{id}")
    public <T> ModelAndView team(@PathVariable int id,
                             Principal principal,
                             Model user,
                             Model players,
                             Model team,
                             Model matches,
                                 Model history) {
        user.addAttribute("user", userService.getUserByName(principal.getName()));
        team.addAttribute("team", teamService.getTeamById(id));
        matches.addAttribute("matchHistory", teamMatchStatService.getMatchesHistoryByTeamId(id));
        players.addAttribute("players", playerService.getPlayersByTeamId(id));

        List<PlayOffMatchService.MatchInfo> matchInfos = new ArrayList<>();
        matchInfos.addAll(playOffMatchService.getMatchInfos());
        matchInfos.addAll(groupStageMatchService.getMatchInfos());

        for (int i = 0; i < matchInfos.size(); i++) {
            if (matchInfos.get(i).getPlayOffStage() == null) {
                matchInfos.get(i).setPlayOffStage(new PlayOffStage(-1));
            }
            if (matchInfos.get(i).teamOwner.getTeam().getId() != id && matchInfos.get(i).teamGuest.getTeam().getId() != id) {
                matchInfos.remove(matchInfos.get(i));
                i--;
            }
        }

        for (int i = 0; i < matchInfos.size() - 1; i++) {
            for (int j = i + 1; j < matchInfos.size(); j++) {
                if (matchInfos.get(i).getMatch().getDate().compareTo(matchInfos.get(j).getMatch().getDate()) < 0) {
                    Collections.swap(matchInfos, i, j);
                }
            }
        }
        history.addAttribute("history", matchInfos);

        return new ModelAndView("team");
    }
}
