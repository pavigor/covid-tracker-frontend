package com.epam.devops.stream17.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {

    private static final Logger logger = LoggerFactory.getLogger(DataService.class);

    @Autowired
    private CovidDataRepository repository;

    public List<CovidData> findAll(int pageNumber, int rowPerPage) {
        List<CovidData> result = new ArrayList<>();
        Pageable sorted = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("date").descending());
        repository.findAll(sorted).forEach(result::add);
        return result;
    }

    public List<CovidData> findByDate(String date) {
        return repository.findDeathDataByDate(date);
    }

    public Long count() {
        return repository.count();
    }
}
