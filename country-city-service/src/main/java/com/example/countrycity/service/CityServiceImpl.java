package com.example.countrycity.service;

import com.example.countrycity.dto.CityRequest;
import com.example.countrycity.dto.CityResponse;
import com.example.countrycity.entity.City;
import com.example.countrycity.entity.Country;
import com.example.countrycity.exception.BadRequestException;
import com.example.countrycity.exception.ResourceNotFoundException;
import com.example.countrycity.repository.CityRepository;
import com.example.countrycity.repository.CountryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public Page<CityResponse> getCitiesByCountryId(Long countryId, Pageable pageable) {
        if (!countryRepository.existsById(countryId)) {
            throw new ResourceNotFoundException("Country not found with id: " + countryId);
        }
        return cityRepository.findByCountryId(countryId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public CityResponse getCityById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
        return mapToResponse(city);
    }

    @Override
    @Transactional
    public CityResponse createCity(Long countryId, String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            throw new BadRequestException("City name cannot be empty");
        }

        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + countryId));

        String trimmedCityName = cityName.trim();
        if (cityRepository.existsByNameAndCountryId(trimmedCityName, countryId)) {
            throw new BadRequestException("City '" + trimmedCityName + "' already exists in country id: " + countryId);
        }

        City city = City.builder()
                .name(trimmedCityName)
                .country(country)
                .population(0L) // Default population
                .zipCode("N/A")
                .description("Created via simple path assignment")
                .build();

        City savedCity = cityRepository.save(city);
        return mapToResponse(savedCity);
    }

    @Override
    @Transactional
    public CityResponse createCityWithDetails(Long countryId, CityRequest cityRequest) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + countryId));

        String trimmedCityName = cityRequest.getName().trim();
        if (cityRepository.existsByNameAndCountryId(trimmedCityName, countryId)) {
            throw new BadRequestException("City '" + trimmedCityName + "' already exists in country id: " + countryId);
        }

        City city = City.builder()
                .name(trimmedCityName)
                .country(country)
                .population(cityRequest.getPopulation())
                .zipCode(cityRequest.getZipCode())
                .description(cityRequest.getDescription())
                .build();

        City savedCity = cityRepository.save(city);
        return mapToResponse(savedCity);
    }

    private CityResponse mapToResponse(City city) {
        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .countryId(city.getCountry().getId())
                .countryName(city.getCountry().getName())
                .population(city.getPopulation())
                .zipCode(city.getZipCode())
                .description(city.getDescription())
                .build();
    }
}
