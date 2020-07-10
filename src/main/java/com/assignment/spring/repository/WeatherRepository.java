package com.assignment.spring.repository;

import com.assignment.spring.entity.WeatherEntity;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends CrudRepository<WeatherEntity, Integer> {

    List<WeatherEntity> findByCity(String city);

    List<WeatherEntity> findTopByAndCityOrderByIdDesc(String city);

}
