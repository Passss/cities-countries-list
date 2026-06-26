package com.example.countrycity.repository;

import com.example.countrycity.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Page<City> findByCountryId(Long countryId, Pageable pageable);
    boolean existsByNameAndCountryId(String name, Long countryId);
}
