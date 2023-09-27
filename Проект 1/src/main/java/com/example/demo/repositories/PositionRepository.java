package com.example.demo.repositories;

import com.example.demo.models.PlayerPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<PlayerPosition, Long> {

}
