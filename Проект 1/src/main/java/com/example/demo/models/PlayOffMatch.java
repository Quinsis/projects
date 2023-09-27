package com.example.demo.models;

import com.example.demo.services.PlayOffMatchService;
import com.example.demo.services.PlayOffStageService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "play_off_match")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayOffMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "tour")
    private int tour;

    @Column(name = "play_off_stage_id")
    private int playOffStageId;

    @OneToOne(optional=false, cascade=CascadeType.REFRESH, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "match_id")
    private Match match;

    public PlayOffMatch(int playOffStageId, Match match, int tour) {
        this.playOffStageId = playOffStageId;
        this.match = match;
        this.tour = tour;
    }
}
