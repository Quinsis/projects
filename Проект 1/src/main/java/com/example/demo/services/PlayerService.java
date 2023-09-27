package com.example.demo.services;

import com.example.demo.models.Player;
import com.example.demo.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    public List<Player> getPlayers() {
        return this.playerRepository.findAll();
    }

    public Player getPlayerById(int id) {
        for (Player player : this.playerRepository.findAll()) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }

    public List<Player> getPlayersByTeamId(int id) {
        List<Player> players = new ArrayList<>();
        for (Player player : this.playerRepository.findAll()) {
            if (player.getTeam().getId() == id) {
                players.add(player);
            }
        }

        String[] order = new String[]{"ВРТ", "ЛЗ", "ЛФЗ", "ЦЗ", "ПЗ", "ОПЗ", "ПФЗ", "ЛПЗ", "ЦПЗ", "ППЗ", "АПЗ", "ОФ", "ЛФА", "ЛФД", "ПФА", "ПФД", "ЦФД", "ФРВ"};

        for (String position : order) {
            List<Integer> founded = new ArrayList<>();
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getPlayerPosition().getName().equals(position)) {
                    for (int j = i; j < players.size() - 1; j++) {
                        Collections.swap(players, j, j + 1);
                    }
                    boolean isFound = false;
                    for (Integer i1 : founded) {
                        if (i == i1) {
                            isFound = true;
                        }
                    }
                    if (!isFound) {
                        founded.add(i);
                        i--;
                    }
                }
            }
        }

        return players;
    }

    public void deletePlayer(Player player) {
        this.playerRepository.delete(player);
    }

    public void createPlayer(Player player) {
        this.playerRepository.save(player);
    }
}
