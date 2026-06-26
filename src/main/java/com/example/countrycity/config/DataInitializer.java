package com.example.countrycity.config;

import com.example.countrycity.entity.City;
import com.example.countrycity.entity.Country;
import com.example.countrycity.repository.CityRepository;
import com.example.countrycity.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    public DataInitializer(CountryRepository countryRepository, CityRepository cityRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public void run(String... args) {
        if (countryRepository.count() == 0) {
            log.info("Database is empty. Initializing countries and cities...");

            // Countries
            Country india = new Country("India");
            Country usa = new Country("United States");
            Country uk = new Country("United Kingdom");
            Country japan = new Country("Japan");

            countryRepository.saveAll(List.of(india, usa, uk, japan));

            // Cities
            City mumbai = City.builder()
                    .name("Mumbai")
                    .country(india)
                    .population(20000000L)
                    .zipCode("400001")
                    .description("Financial capital of India, home of Bollywood.")
                    .build();

            City delhi = City.builder()
                    .name("Delhi")
                    .country(india)
                    .population(19000000L)
                    .zipCode("110001")
                    .description("Capital city of India, rich in historical heritage.")
                    .build();

            City nyc = City.builder()
                    .name("New York")
                    .country(usa)
                    .population(8400000L)
                    .zipCode("10001")
                    .description("The city that never sleeps, global financial and cultural hub.")
                    .build();

            City la = City.builder()
                    .name("Los Angeles")
                    .country(usa)
                    .population(3900000L)
                    .zipCode("90001")
                    .description("Center of the nation's film and television industry.")
                    .build();

            City london = City.builder()
                    .name("London")
                    .country(uk)
                    .population(8900000L)
                    .zipCode("EC1A")
                    .description("Capital of the UK, historic city dating back to Roman times.")
                    .build();

            City tokyo = City.builder()
                    .name("Tokyo")
                    .country(japan)
                    .population(14000000L)
                    .zipCode("100-0001")
                    .description("Bustling metropolis, blending ultramodern and traditional.")
                    .build();

            cityRepository.saveAll(List.of(mumbai, delhi, nyc, la, london, tokyo));
            log.info("Database initialization completed successfully.");
        } else {
            log.info("Database already contains data. Seeding skipped.");
        }
    }
}
