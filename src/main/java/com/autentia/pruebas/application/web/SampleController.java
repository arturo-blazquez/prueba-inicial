package com.autentia.pruebas.application.web;

import com.autentia.pruebas.application.exceptions.SampleAlreadyCreatedException;
import com.autentia.pruebas.application.exceptions.SampleBadRequestException;
import com.autentia.pruebas.application.exceptions.SampleNotFoundException;
import com.autentia.pruebas.application.model.Sample;
import com.autentia.pruebas.application.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/samples")
public class SampleController {
    private final SampleService sampleService;

    @Autowired
    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @GetMapping
    public Page<Sample> getAllSamples(Pageable pageRequest) {
        return sampleService.getAllSamples(pageRequest);
    }

    @GetMapping(value = "/{sampleId}")
    public Sample getSampleById(@PathVariable("sampleId") Long sampleId) throws SampleNotFoundException {
        return sampleService.getSampleById(sampleId);
    }

    @PostMapping
    public ResponseEntity<Sample> addSample(@RequestBody Sample sample) throws SampleAlreadyCreatedException {
        Sample createdSample = sampleService.addSample(sample);

        return ResponseEntity.created(getUri(createdSample)).contentType(MediaType.APPLICATION_JSON).body(createdSample);
    }

    @PutMapping(value = "/{sampleId}")
    public Sample updateSample(@PathVariable("sampleId") Long userId, @RequestBody Sample sample) throws SampleNotFoundException, SampleBadRequestException {
        if (!userId.equals(sample.getId())) {
            throw new SampleBadRequestException();
        }
        return sampleService.updateSample(sample);
    }

    @DeleteMapping(value = "/{sampleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSample(@PathVariable("sampleId") Long sampleId) throws SampleNotFoundException {
        sampleService.deleteSample(sampleId);
    }

    @ExceptionHandler(SampleNotFoundException.class)
    public ResponseEntity<Object> sampleNotFoundException() {
        return new ResponseEntity<>(SampleNotFoundException.ERROR_MESSAGE, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SampleAlreadyCreatedException.class)
    public ResponseEntity<Object> sampleAlreadyCreatedException() {
        return new ResponseEntity<>(SampleAlreadyCreatedException.ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SampleBadRequestException.class)
    public ResponseEntity<Object> sampleBadRequestException() {
        return new ResponseEntity<>(SampleBadRequestException.ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
    }


    private URI getUri(Sample createdSample) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdSample.getId()).toUri();
    }
}