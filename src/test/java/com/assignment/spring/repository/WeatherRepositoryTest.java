package com.assignment.spring.repository;

import static com.assignment.spring.dto.Unit.METRIC;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.assignment.spring.config.TestJpaConfig;
import com.assignment.spring.entity.WeatherEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestJpaConfig.class)
public class WeatherRepositoryTest {


    @Autowired
    private WeatherRepository weatherRepository;

    @BeforeEach
    public void setUp() {
        weatherRepository.deleteAll();
    }

    @Test
    public void testSave() {
        WeatherEntity entity = WeatherEntity.builder()
            .city("Amsterdam")
            .country("Netherlands")
            .temperature(17.2)
            .unit(METRIC.toString())
            .build();


        final WeatherEntity actual = weatherRepository.save(entity);

        assertEquals("Amsterdam", actual.getCity());
        assertEquals("Netherlands", actual.getCountry());
        assertEquals(17.2, actual.getTemperature());
        assertEquals(METRIC.toString(), actual.getUnit());
    }

    @Test
    public void testFindByCity() {


        WeatherEntity entity1 = WeatherEntity.builder()
            .city("Amsterdam")
            .country("Netherlands")
            .temperature(17.1)
            .unit(METRIC.toString())
            .build();
        weatherRepository.save(entity1);


        WeatherEntity entity2 = WeatherEntity.builder()
            .city("Amsterdam")
            .country("Netherlands")
            .temperature(17.2)
            .unit(METRIC.toString())
            .build();
        weatherRepository.save(entity2);

        WeatherEntity entity3 = WeatherEntity.builder()
            .city("Amsterdam")
            .country("Netherlands")
            .temperature(17.3)
            .unit(METRIC.toString())
            .build();
        weatherRepository.save(entity3);

        final List<WeatherEntity> all = weatherRepository.findByCity("Amsterdam");

        assertEquals(3, all.size());

    }

    @Test
    public void testMostRecentForCity() {

        WeatherEntity entity1 = WeatherEntity.builder()
            .city("Amsterdam")
            .country("Netherlands")
            .temperature(17.1)
            .unit(METRIC.toString())
            .build();
        weatherRepository.save(entity1);


        WeatherEntity entity2 = WeatherEntity.builder()
            .city("Amsterdam")
            .country("Netherlands")
            .temperature(17.2)
            .unit(METRIC.toString())
            .build();
        weatherRepository.save(entity2);

        WeatherEntity entity3 = WeatherEntity.builder()
            .city("Amsterdam")
            .country("Netherlands")
            .temperature(17.3)
            .unit(METRIC.toString())
            .build();
        weatherRepository.save(entity3);


        final List<WeatherEntity> all = weatherRepository
            .findTopByAndCityOrderByIdDesc("Amsterdam");

        final WeatherEntity actual = all.get(0);

        assertEquals(1, all.size());
        assertEquals("Amsterdam", actual.getCity());
        assertEquals("Netherlands", actual.getCountry());
        assertEquals(17.3, actual.getTemperature());
        assertEquals(METRIC.toString(), actual.getUnit());
    }


}
