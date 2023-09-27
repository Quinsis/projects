package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tournament")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "date_begin")
    private String date_begin;

    @Column(name = "date_end")
    private String date_end;

    @Column(name = "photo")
    private String photo;

    @OneToMany(cascade = CascadeType.ALL)
    private List<GroupStage> groupStages;

    @OneToOne(cascade = CascadeType.ALL)
    private PlayOff playOff;

    public Tournament(String name, String date_begin, String date_end, String photo) {
        this.name = name;
        this.date_begin = date_begin;
        this.date_end = date_end;
        this.photo = photo;
    }
}
