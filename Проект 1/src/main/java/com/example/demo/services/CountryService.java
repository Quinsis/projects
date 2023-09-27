package com.example.demo.services;

import com.example.demo.models.Country;
import com.example.demo.repositories.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    public List<Country> getCountries() {
        return sortCountry();
    }

    public List<Country> sortCountry() {
        List<Country> countries = this.countryRepository.findAll();
        for (int i = 0; i < countries.size(); i++) {
            for (int j = 0; j < countries.size(); j++) {
                if (countries.get(i).getName().compareTo(countries.get(j).getName()) < 0) {
                    Collections.swap(countries, i, j);
                }
            }
        }
        return countries;
    }

    public Country getCountryByName(String name) {
        for (Country country : countryRepository.findAll()) {
            if (country.getName().equals(name)) {
                return country;
            }
        }
        return null;
    }
}
