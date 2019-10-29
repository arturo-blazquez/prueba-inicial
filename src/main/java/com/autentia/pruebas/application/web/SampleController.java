package com.autentia.pruebas.application.web;

import com.autentia.pruebas.application.exceptions.SampleAlreadyCreatedException;
import com.autentia.pruebas.application.exceptions.SampleNotFoundException;
import com.autentia.pruebas.application.model.Sample;
import com.autentia.pruebas.application.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/application")
public class SampleController {
    private final SampleService sampleService;

    @Autowired
    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @GetMapping(value = "/getAll")
    public Page<Sample> getAllSamples(Pageable pageRequest) {
        return sampleService.getAllSamples(pageRequest);
    }

    @GetMapping(value = "/get/{sampleId}")
    public Sample getSampleById(@PathVariable("sampleId") Long sampleId) throws SampleNotFoundException {
        return sampleService.getSampleById(sampleId);
    }

    @PostMapping(value = "/add")
    public Sample addSample(@RequestBody Sample sample) throws SampleAlreadyCreatedException {
        return sampleService.addSample(sample);
    }

    @PutMapping(value = "/update/{sampleId}")
    public Sample updateSample(@PathVariable("sampleId") Long userId, @RequestBody String newName) throws SampleNotFoundException {
        return sampleService.updateSample(userId, newName);
    }

    @DeleteMapping(value = "/delete/{sampleId}")
    public Sample deleteSample(@PathVariable("sampleId") Long sampleId) throws SampleNotFoundException {
        return sampleService.deleteSample(sampleId);
    }

    @ExceptionHandler(SampleNotFoundException.class)
    public ResponseEntity<Object> SampleNotFoundException() {
        return new ResponseEntity<>(SampleNotFoundException.ERROR_MESSAGE, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SampleAlreadyCreatedException.class)
    public ResponseEntity<Object> SampleAlreadyCreatedException() {
        return new ResponseEntity<>(SampleAlreadyCreatedException.ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
    }
}