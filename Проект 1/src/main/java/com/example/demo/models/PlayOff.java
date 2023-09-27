package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "play_off")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayOff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(optional=false, cascade=CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PlayOffStage> playOffStages;

    public PlayOff(Tournament tournament) {
        this.tournament = tournament;
        this.playOffStages = new ArrayList<>();
    }
}
