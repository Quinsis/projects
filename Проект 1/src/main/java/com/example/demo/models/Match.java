package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "match")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "date")
    private String date;

    @OneToOne(cascade = CascadeType.ALL)
    private PlayOffMatch playOffMatch;

    @OneToOne(cascade = CascadeType.ALL)
    private GroupStageMatch groupStageMatch;

    public Match(String matchDate) {
        this.date = matchDate;
    }
}
