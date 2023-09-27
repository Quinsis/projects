package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "group_stage_match")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupStageMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(optional=false, cascade=CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_stage_id")
    private GroupStage groupStage;

    @OneToOne(optional=false, cascade=CascadeType.REFRESH, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "match_id")
    private Match match;

    public GroupStageMatch(GroupStage groupStage, Match match) {
        this.groupStage = groupStage;
        this.match = match;
    }
}
