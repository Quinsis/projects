package com.example.demo.repositories;

import com.example.demo.models.TeamMatchStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMatchStatRepository extends JpaRepository<TeamMatchStat, Long> {
}
