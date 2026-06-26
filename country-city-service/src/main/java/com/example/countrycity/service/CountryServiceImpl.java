package com.example.countrycity.service;

import com.example.countrycity.dto.CountryResponse;
import com.example.countrycity.entity.Country;
import com.example.countrycity.exception.BadRequestException;
import com.example.countrycity.exception.ResourceNotFoundException;
import com.example.countrycity.repository.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<CountryResponse> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CountryResponse getCountryById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + id));
        return mapToResponse(country);
    }

    @Override
    @Transactional
    public CountryResponse createCountry(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("Country name cannot be empty");
        }
        
        String trimmedName = name.trim();
        if (countryRepository.existsByName(trimmedName)) {
            throw new BadRequestException("Country with name '" + trimmedName + "' already exists");
        }

        Country country = new Country(trimmedName);
        
        Country savedCountry = countryRepository.save(country);
        return mapToResponse(savedCountry);
    }

    private CountryResponse mapToResponse(Country country) {
        return CountryResponse.builder()
                .id(country.getId())
                .name(country.getName())
                .build();
    }
}
