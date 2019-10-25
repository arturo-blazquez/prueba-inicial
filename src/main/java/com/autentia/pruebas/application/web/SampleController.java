package com.autentia.pruebas.application.web;

import com.autentia.pruebas.application.model.Sample;
import com.autentia.pruebas.application.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
public class SampleController {

    @Autowired
    private SampleService sampleService;

    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @RequestMapping(value = "/application/getAll", method = RequestMethod.GET)
    public Page<Sample> getAllSamples(Pageable pageRequest) {
        return sampleService.getAllSamples(pageRequest.getPageNumber(), pageRequest.getPageSize(), pageRequest.getSort());
    }

    @RequestMapping(value = "/application/get/{sampleId}", method = RequestMethod.GET)
    public Sample getSampleById(@PathVariable("sampleId") Long sampleId) {
        return sampleService.getSampleById(sampleId);
    }

    @RequestMapping(value = "/application/add", method = RequestMethod.POST)
    public Sample addSample(@RequestBody Sample sample) {
        return sampleService.addSample(sample);
    }

    @RequestMapping(value = "/application/update/{sampleId}", method = RequestMethod.PUT)
    public Sample updateSample(@PathVariable("sampleId") Long userId, @RequestBody String newName) {
        return sampleService.updateSample(userId, newName);
    }

    @RequestMapping(value = "/application/delete/{sampleId}", method = RequestMethod.DELETE)
    public Sample deleteSample(@PathVariable("sampleId") Long sampleId) {
        return sampleService.deleteSample(sampleId);
    }

}