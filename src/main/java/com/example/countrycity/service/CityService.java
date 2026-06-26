package com.example.countrycity.service;

import com.example.countrycity.dto.CityRequest;
import com.example.countrycity.dto.CityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityService {
    Page<CityResponse> getCitiesByCountryId(Long countryId, Pageable pageable);
    CityResponse getCityById(Long id);
    CityResponse createCity(Long countryId, String cityName);
    CityResponse createCityWithDetails(Long countryId, CityRequest cityRequest);
}
