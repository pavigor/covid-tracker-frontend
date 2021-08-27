package com.epam.devops.stream17.data;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CovidDataRepository extends PagingAndSortingRepository<CovidData, Long>, JpaSpecificationExecutor<CovidData> {
    List<CovidData> findDeathDataByDate(String date);


}
