package com.autentia.pruebas.application.service;

import com.autentia.pruebas.application.model.Sample;
import com.autentia.pruebas.application.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SampleService {

    @Autowired
    private SampleRepository sampleRepository;

    public SampleService(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    public Page<Sample> getAllSamples() {
        return getAllSamples(0, 10, Sort.by("id").descending());
    }

    public Page<Sample> getAllSamples(int page, int size, Sort sort) {
        Pageable askedPage = PageRequest.of(page, size, sort);

        return sampleRepository.findAll(askedPage);
    }

    public Sample getSampleById(Long sampleId) throws RuntimeException {
        return sampleRepository.findById(sampleId).orElseThrow(() -> new RuntimeException("Sample no encontrado"));
    }

    public Sample addSample(Sample sample) throws RuntimeException {
        if (sampleRepository.findById(sample.getId()).isPresent()) {
            throw new RuntimeException("Sample ya en la base de datos");
        } else {
            sampleRepository.save(sample);
            return sample;
        }
    }

    public Sample updateSample(Long sampleId, String newName) throws RuntimeException {
        Sample sampleToUpdate = sampleRepository.findById(sampleId).orElseThrow(() -> new RuntimeException("Sample no existe en la base de datos"));

        sampleToUpdate.setName(newName);

        sampleRepository.save(sampleToUpdate);
        return sampleToUpdate;
    }

    public Sample deleteSample(Long sampleId) throws RuntimeException {
        Sample sampleToDelete = sampleRepository.findById(sampleId).orElseThrow(() -> new RuntimeException("Sample no existe en la base de datos"));

        sampleRepository.delete(sampleToDelete);
        return sampleToDelete;
    }
}