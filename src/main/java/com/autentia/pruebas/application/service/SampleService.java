package com.autentia.pruebas.application.service;

import com.autentia.pruebas.application.exceptions.SampleAlreadyCreatedException;
import com.autentia.pruebas.application.exceptions.SampleNotFoundException;
import com.autentia.pruebas.application.model.Sample;
import com.autentia.pruebas.application.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SampleService {
    private final SampleRepository sampleRepository;

    @Autowired
    public SampleService(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    public Page<Sample> getAllSamples(Pageable pageRequest) {
        return sampleRepository.findAll(pageRequest);
    }

    public Sample getSampleById(Long sampleId) throws SampleNotFoundException {
        return sampleRepository.findById(sampleId).orElseThrow(SampleNotFoundException::new);
    }

    public Sample addSample(Sample sample) throws SampleAlreadyCreatedException {
        if (sampleRepository.findById(sample.getId()).isPresent()) {
            throw new SampleAlreadyCreatedException();
        } else {
            sampleRepository.save(sample);
            return sample;
        }
    }

    public Sample updateSample(Sample sample) throws SampleNotFoundException {
        Sample sampleToUpdate = sampleRepository.findById(sample.getId()).orElseThrow(SampleNotFoundException::new);

        sampleToUpdate.setName(sample.getName());

        sampleRepository.save(sampleToUpdate);
        return sampleToUpdate;
    }

    public void deleteSample(Long sampleId) throws SampleNotFoundException {
        Sample sampleToDelete = sampleRepository.findById(sampleId).orElseThrow(SampleNotFoundException::new);
        sampleRepository.delete(sampleToDelete);
    }
}