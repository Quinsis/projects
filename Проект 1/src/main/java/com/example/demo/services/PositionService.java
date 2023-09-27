package com.example.demo.services;

import com.example.demo.models.Country;
import com.example.demo.models.PlayerPosition;
import com.example.demo.repositories.PositionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class PositionService {
    private final PositionRepository positionRepository;

    public List<PlayerPosition> getPlayerPositions() {
        return this.positionRepository.findAll();
    }

    public PlayerPosition getPlayerPositionByName(String name) {
        for (PlayerPosition playerPosition : positionRepository.findAll()) {
            if (playerPosition.getName().equals(name)) {
                return playerPosition;
            }
        }
        return null;
    }
}
