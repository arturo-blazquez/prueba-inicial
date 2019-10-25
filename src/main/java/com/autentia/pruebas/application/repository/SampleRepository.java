package com.autentia.pruebas.application.repository;

import com.autentia.pruebas.application.model.Sample;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepository extends PagingAndSortingRepository<Sample, Long> {

}