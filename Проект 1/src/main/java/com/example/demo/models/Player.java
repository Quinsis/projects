package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "player")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne(optional=false, cascade=CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(optional=false, cascade=CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_position_id")
    private PlayerPosition playerPosition;

    public Player(Person person, Team team, PlayerPosition playerPosition) {
        this.person = person;
        this.team = team;
        this.playerPosition = playerPosition;
    }
}
