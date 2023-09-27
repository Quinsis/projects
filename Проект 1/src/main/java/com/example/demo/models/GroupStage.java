package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "group_stage")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(optional=false, cascade=CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<GroupStageMatch> groupStageMatches;

    public GroupStage(String name, Tournament tournament) {
        this.name = name;
        this.tournament = tournament;
    }
}
