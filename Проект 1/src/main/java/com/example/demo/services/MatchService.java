package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.repositories.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class MatchService {
    private final MatchRepository matchRepository;
    public Match getMatchById(int id) {
        for (Match match : this.matchRepository.findAll()) {
            if (match.getId() == id) {
                return match;
            }
        }
        return null;
    }

    public List<Match> getMatches() {
        return matchRepository.findAll();
    }

    public void addMatch(Match match) {
        this.matchRepository.save(match);
    }

    public void deleteMatch(Match match) {
        this.matchRepository.delete(match);
    }
}
