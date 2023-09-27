package com.example.demo.services;

import com.example.demo.models.PlayOff;
import com.example.demo.repositories.PlayOffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class PlayOffService {
    private final PlayOffRepository playOffRepository;

    public void createPlayOff(PlayOff playOff) {
        this.playOffRepository.save(playOff);
    }
    public PlayOff getPlayOffByTournamentId(int tournamentId) {
        for (PlayOff playOff : playOffRepository.findAll()) {
            if (playOff.getTournament().getId() == tournamentId) {
                return playOff;
            }
        }
        return null;
    }
}
