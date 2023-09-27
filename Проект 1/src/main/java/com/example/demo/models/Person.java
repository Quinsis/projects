package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "person")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "country_id")
    private int countryId;

    @Column(name = "photo")
    private String photo;

    @OneToOne(cascade = CascadeType.ALL)
    private Player player;

    public Person(String name, String surname, String patronymic, int countryId, String photo) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.countryId = countryId;
        this.photo = photo;
    }
}
