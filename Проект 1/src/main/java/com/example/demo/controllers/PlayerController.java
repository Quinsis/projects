package com.example.demo.controllers;

import com.example.demo.models.Person;
import com.example.demo.models.Player;
import com.example.demo.models.Team;
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

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class PlayerController {
    private final PersonService personService;
    private final UserService userService;
    private final TeamService teamService;
    private final PositionService positionService;
    private final CountryService countryService;
    private final PlayerService playerService;

    @GetMapping("/team/{teamId}/player/create")
    public ModelAndView create(@PathVariable int teamId,
                               Model team,
                               Principal principal,
                               Model user,
                               Model positions,
                               Model countries) {
        team.addAttribute("team", teamService.getTeamById(teamId));
        user.addAttribute("user", userService.getUserByName(principal.getName()));
        positions.addAttribute("positions", positionService.getPlayerPositions());
        countries.addAttribute("countries", countryService.getCountries());
        return new ModelAndView("createPlayer");
    }

    @PostMapping("/team/{teamId}/createPlayer")
    public ModelAndView create(@PathVariable int teamId,
                               @RequestParam ("name") String name,
                               @RequestParam ("surname") String surname,
                               @RequestParam ("patronymic") String patronymic,
                               @RequestParam ("position") String position,
                               @RequestParam ("country") String country,
                               @RequestParam ("photo") String photo) {
        if (photo.length() == 0) { photo = "notfound.png"; }
        Person person = new Person(name, surname, patronymic, countryService.getCountryByName(country).getId(),"/static/img/players/" + photo);
        personService.createPerson(person);
        Player player = new Player(personService.getPersons().get(personService.getPersons().size() - 1), teamService.getTeamById(teamId), positionService.getPlayerPositionByName(position));
        playerService.createPlayer(player);
        return new ModelAndView("redirect:/team/" + player.getTeam().getId());
    }

    @GetMapping("/player/{playerId}/delete")
    public ModelAndView delete(@PathVariable int playerId) {
        Team team = playerService.getPlayerById(playerId).getTeam();
        playerService.deletePlayer(playerService.getPlayerById(playerId));
        return new ModelAndView("redirect:/team/" + team.getId());
    }
}
