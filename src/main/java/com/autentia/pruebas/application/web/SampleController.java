package com.autentia.pruebas.application.web;

import com.autentia.pruebas.application.exceptions.SampleAlreadyCreatedException;
import com.autentia.pruebas.application.exceptions.SampleNotFoundException;
import com.autentia.pruebas.application.model.Sample;
import com.autentia.pruebas.application.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
public class SampleController {
    private final SampleService sampleService;

    @Autowired
    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @GetMapping(value = "/application/getAll")
    public Page<Sample> getAllSamples(Pageable pageRequest) {
        return sampleService.getAllSamples(pageRequest);
    }

    @GetMapping(value = "/application/get/{sampleId}")
    public Sample getSampleById(@PathVariable("sampleId") Long sampleId) throws SampleNotFoundException {
        return sampleService.getSampleById(sampleId);
    }

    @PostMapping(value = "/application/add")
    public Sample addSample(@RequestBody Sample sample) throws SampleAlreadyCreatedException {
        return sampleService.addSample(sample);
    }

    @PutMapping(value = "/application/update/{sampleId}")
    public Sample updateSample(@PathVariable("sampleId") Long userId, @RequestBody String newName) throws SampleNotFoundException {
        return sampleService.updateSample(userId, newName);
    }

    @DeleteMapping(value = "/application/delete/{sampleId}")
    public Sample deleteSample(@PathVariable("sampleId") Long sampleId) throws SampleNotFoundException {
        return sampleService.deleteSample(sampleId);
    }

}