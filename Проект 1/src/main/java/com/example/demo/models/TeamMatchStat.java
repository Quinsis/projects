package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "team_match_stat")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamMatchStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "team_id")
    private int teamId;
    @Column(name = "match_id")
    private int matchId;
    @Column(name = "goals")
    private int goals;
    @Column(name = "total_attempts")
    private int totalAttempts;
    @Column(name = "goal_attempts")
    private int goalAttempts;
    @Column(name = "possession")
    private int possession;
    @Column(name = "passes")
    private int passes;
    @Column(name = "passes_accuracy")
    private int passesAccuracy;
    @Column(name = "fouls")
    private int fouls;
    @Column(name = "offsides")
    private int offsides;
    @Column(name = "corners")
    private int corners;
    @Column(name = "yellow_cards")
    private int yellowCards;
    @Column(name = "red_cards")
    private int redCards;

    public TeamMatchStat(int ownerStat, int matchId, int goals, int totalAttempts, int goalAttempts, int possession, int passes, int passesAccuracy, int fouls, int yellowCards, int redCards, int offsides, int corners) {
        this.teamId = ownerStat;
        this.matchId = matchId;
        this.goals = goals;
        this.totalAttempts = totalAttempts;
        this.goalAttempts = goalAttempts;
        this.possession = possession;
        this.passes = passes;
        this.passesAccuracy = passesAccuracy;
        this.fouls = fouls;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
        this.offsides = offsides;
        this.corners = corners;
    }
}
