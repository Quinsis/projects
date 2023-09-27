package com.example.demo.services;

import com.example.demo.models.Person;
import com.example.demo.models.Player;
import com.example.demo.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class PersonService {
    private final PersonRepository personRepository;

    public List<Person> getPersons() {
        return this.personRepository.findAll();
    }

    public Person getPersonById(int id) {
        for (Person person : this.personRepository.findAll()) {
            if (person.getId() == id) {
                return person;
            }
        }
        return null;
    }

    public void createPerson(Person person) {
        this.personRepository.save(person);
    }

    public void deletePerson(Person person) {
        this.personRepository.delete(person);
    }
}
