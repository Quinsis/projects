package com.example.demo.services;

import com.example.demo.models.PlayOffStage;
import com.example.demo.repositories.PlayOffStageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class PlayOffStageService {
    private final PlayOffStageRepository playOffStageRepository;

    public List<PlayOffStage> getPlayOffStagesByTournamentId(int id) {
        List<PlayOffStage> playOffStages = new ArrayList<>();
        for (PlayOffStage playOffStage : this.playOffStageRepository.findAll()) {
            if (playOffStage.getPlayOff().getTournament().getId() == id) {
                playOffStages.add(playOffStage);
            }
        }
        return playOffStages;
    }

    public PlayOffStage getPlayOffStagesById(int id) {
        for (PlayOffStage playOffStage : this.playOffStageRepository.findAll()) {
            if (playOffStage.getId() == id) {
                return playOffStage;
            }
        }
        return null;
    }

    public PlayOffStage getPlayOffStagesByName(String name) {
        for (PlayOffStage playOffStage : this.playOffStageRepository.findAll()) {
            if (playOffStage.getName().equals(name)) {
                return playOffStage;
            }
        }
        return null;
    }

    public String getNameOfPlayOffStageById(int playOffStageId) {
        for (PlayOffStage playOffStage : playOffStageRepository.findAll()) {
            if (playOffStage.getId() == playOffStageId) {
                return playOffStage.getName();
            }
        }
        return null;
    }

    public void createPlayOffStage(PlayOffStage playOffStage) {
        this.playOffStageRepository.save(playOffStage);
    }
}
