package com.example.countrycity.service;

import com.example.countrycity.dto.CountryResponse;

import java.util.List;

public interface CountryService {
    List<CountryResponse> getAllCountries();
    CountryResponse getCountryById(Long id);
    CountryResponse createCountry(String name);
}
