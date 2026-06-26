package com.example.countrycity.service;

import com.example.countrycity.dto.CityRequest;
import com.example.countrycity.dto.CityResponse;
import com.example.countrycity.entity.City;
import com.example.countrycity.entity.Country;
import com.example.countrycity.exception.BadRequestException;
import com.example.countrycity.exception.ResourceNotFoundException;
import com.example.countrycity.repository.CityRepository;
import com.example.countrycity.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CityServiceImpl cityService;

    private Country country;
    private City city;

    @BeforeEach
    void setUp() {
        country = Country.builder()
                .id(1L)
                .name("France")
                .build();

        city = City.builder()
                .id(10L)
                .name("Paris")
                .country(country)
                .population(2100000L)
                .zipCode("75001")
                .description("City of Light")
                .build();
    }

    @Test
    void getCityById_WhenExists_ShouldReturnCityResponse() {
        // Arrange
        when(cityRepository.findById(10L)).thenReturn(Optional.of(city));

        // Act
        CityResponse response = cityService.getCityById(10L);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getName()).isEqualTo("Paris");
        assertThat(response.getCountryName()).isEqualTo("France");
    }

    @Test
    void getCityById_WhenNotExists_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(cityRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> cityService.getCityById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("City not found with id: 99");
    }

    @Test
    void getCitiesByCountryId_WhenCountryNotExists_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(countryRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> cityService.getCitiesByCountryId(99L, Pageable.unpaged()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Country not found with id: 99");
    }

    @Test
    void getCitiesByCountryId_WhenExists_ShouldReturnPaginatedCities() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<City> cityPage = new PageImpl<>(Collections.singletonList(city), pageable, 1);
        
        when(countryRepository.existsById(1L)).thenReturn(true);
        when(cityRepository.findByCountryId(1L, pageable)).thenReturn(cityPage);

        // Act
        Page<CityResponse> responses = cityService.getCitiesByCountryId(1L, pageable);

        // Assert
        assertThat(responses).isNotNull();
        assertThat(responses.getTotalElements()).isEqualTo(1);
        assertThat(responses.getContent().get(0).getName()).isEqualTo("Paris");
    }

    @Test
    void createCity_WhenNameIsEmpty_ShouldThrowBadRequestException() {
        // Act & Assert
        assertThatThrownBy(() -> cityService.createCity(1L, ""))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("City name cannot be empty");
    }

    @Test
    void createCity_WhenCountryNotExists_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(countryRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> cityService.createCity(99L, "Paris"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Country not found with id: 99");
    }

    @Test
    void createCity_WhenCityNameAlreadyExists_ShouldThrowBadRequestException() {
        // Arrange
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        when(cityRepository.existsByNameAndCountryId("Paris", 1L)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> cityService.createCity(1L, "Paris"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("City 'Paris' already exists in country id: 1");
    }

    @Test
    void createCity_WhenValid_ShouldSaveAndReturnResponse() {
        // Arrange
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        when(cityRepository.existsByNameAndCountryId("Paris", 1L)).thenReturn(false);
        when(cityRepository.save(any(City.class))).thenReturn(city);

        // Act
        CityResponse response = cityService.createCity(1L, "Paris");

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Paris");
        verify(cityRepository, times(1)).save(any(City.class));
    }

    @Test
    void createCityWithDetails_WhenValid_ShouldSaveAndReturnResponse() {
        // Arrange
        CityRequest request = CityRequest.builder()
                .name("Paris")
                .population(2100000L)
                .zipCode("75001")
                .description("City of Light")
                .build();

        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        when(cityRepository.existsByNameAndCountryId("Paris", 1L)).thenReturn(false);
        when(cityRepository.save(any(City.class))).thenReturn(city);

        // Act
        CityResponse response = cityService.createCityWithDetails(1L, request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Paris");
        assertThat(response.getPopulation()).isEqualTo(2100000L);
        assertThat(response.getZipCode()).isEqualTo("75001");
        verify(cityRepository, times(1)).save(any(City.class));
    }
}
