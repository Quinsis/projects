package com.example.demo.repositories;

import com.example.demo.models.League;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<League, Long> {

}
