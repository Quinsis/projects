package com.example.demo.services;

import com.example.demo.models.GroupStage;
import com.example.demo.repositories.GroupStageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class GroupStageService {
    private final GroupStageRepository groupStageRepository;

    public List<GroupStage> getGroupStages() {
        return this.groupStageRepository.findAll();
    }

    public List<GroupStage> getGroupStagesByTournamentId(int id) {
        List<GroupStage> groupStages = new ArrayList<>();
        for (GroupStage groupStage : this.groupStageRepository.findAll()) {
            if (groupStage.getTournament().getId() == id) {
                groupStages.add(groupStage);
            }
        }
        return groupStages;
    }

    public GroupStage getGroupStageById(int id) {
        for (GroupStage groupStage : groupStageRepository.findAll()) {
            if (groupStage.getId() == id) {
                return groupStage;
            }
        }
        return null;
    }

    public void createGroupStage(GroupStage groupStage) {
        this.groupStageRepository.save(groupStage);
    }
}
