package com.autentia.pruebas.application.web;

import com.autentia.pruebas.application.exceptions.SampleAlreadyCreatedException;
import com.autentia.pruebas.application.exceptions.SampleBadRequestException;
import com.autentia.pruebas.application.exceptions.SampleNotFoundException;
import com.autentia.pruebas.application.model.Sample;
import com.autentia.pruebas.application.service.SampleService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class SampleControllerTest {

    private SampleController sampleController;
    private SampleService sampleService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void init() {
        sampleService = mock(SampleService.class);
        sampleController = new SampleController(sampleService);
    }

    @Test
    public void sampleControllerShouldGetAllSamplesWhenSomeExist() {
        Sample sample1 = new Sample(1L, "Juan");
        Sample sample2 = new Sample(2L, "Ana");
        Page<Sample> expectedSamples = new PageImpl<>(List.of(sample1, sample2));
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by("name").descending());

        when(sampleService.getAllSamples(any(Pageable.class))).thenReturn(expectedSamples);

        Page<Sample> samplesFound = sampleController.getAllSamples(pageRequest);

        verify(sampleService).getAllSamples(any(Pageable.class));
        assertEquals(samplesFound, expectedSamples);
    }

    @Test
    public void sampleControllerShouldGetNoSamplesWhenThereAreNone() {
        Page<Sample> emptySamples = Page.empty();
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by("name").descending());

        when(sampleService.getAllSamples(any(Pageable.class))).thenReturn(emptySamples);

        Page<Sample> samplesFound = sampleController.getAllSamples(pageRequest);

        verify(sampleService).getAllSamples(any(Pageable.class));
        assertEquals(samplesFound, emptySamples);
    }

    @Test
    public void sampleControllerShouldGetSampleWhenIdExists() throws SampleNotFoundException {
        Sample sample1 = new Sample(1L, "Juan");

        when(sampleService.getSampleById(anyLong())).thenReturn(sample1);

        Sample sampleFound = sampleController.getSampleById(1L);

        verify(sampleService).getSampleById(anyLong());
        assertEquals(sampleFound, sample1);
    }

    @Test
    public void sampleControllerShouldGetNoSamplesWhenIdDoesNotExist() throws SampleNotFoundException {
        when(sampleService.getSampleById(anyLong())).thenThrow(new SampleNotFoundException());

        thrown.expect(SampleNotFoundException.class);
        thrown.expectMessage(SampleNotFoundException.ERROR_MESSAGE);
        sampleController.getSampleById(1L);

        verify(sampleService).getSampleById(anyLong());
    }

    @Test
    public void sampleControllerShouldAddANewSampleWhenItsDoesntAlreadyExist() throws SampleAlreadyCreatedException {
        Sample sample1 = new Sample(1L, "Juan");

        when(sampleService.addSample(sample1)).thenReturn(sample1);

        Sample sampleAdded = sampleController.addSample(sample1);

        verify(sampleService).addSample(sample1);
        assertEquals(sampleAdded, sample1);
    }

    @Test
    public void sampleControllerShouldNotAddANewSampleWhenItAlreadyExists() throws SampleAlreadyCreatedException {
        Sample sample1 = new Sample(1L, "Juan");

        when(sampleService.addSample(sample1)).thenThrow(new SampleAlreadyCreatedException());

        thrown.expect(SampleAlreadyCreatedException.class);
        thrown.expectMessage(SampleAlreadyCreatedException.ERROR_MESSAGE);
        sampleController.addSample(sample1);

        verify(sampleService).addSample(sample1);
    }

    @Test
    public void sampleControllerShouldUpdateASampleWhenItExists() throws SampleNotFoundException, SampleBadRequestException {
        String newName = "Ana";
        Sample sample2 = new Sample(1L, newName);

        when(sampleService.updateSample(any(Sample.class))).thenReturn(sample2);

        Sample sampleUpdated = sampleController.updateSample(sample2.getId(), sample2);

        verify(sampleService).updateSample(any(Sample.class));
        assertEquals(sampleUpdated, sample2);
    }

    @Test
    public void sampleControllerShouldNotUpdateASampleWhenItDoesNotExists() throws SampleNotFoundException, SampleBadRequestException {
        String newName = "Ana";
        Sample sample2 = new Sample(1L, newName);

        when(sampleService.updateSample(any(Sample.class))).thenThrow(new SampleNotFoundException());

        thrown.expect(SampleNotFoundException.class);
        thrown.expectMessage(SampleNotFoundException.ERROR_MESSAGE);
        sampleController.updateSample(sample2.getId(), sample2);

        verify(sampleService).updateSample(any(Sample.class));
    }

    @Test
    public void sampleControllerShouldNotUpdateASampleWhenSampleIdAndUrlIdDoesntMatch() throws SampleNotFoundException, SampleBadRequestException {
        String newName = "Ana";
        Sample sample2 = new Sample(1L, newName);

        thrown.expect(SampleBadRequestException.class);
        thrown.expectMessage(SampleBadRequestException.ERROR_MESSAGE);
        sampleController.updateSample(2L, sample2);

        verify(sampleService, never()).updateSample(any(Sample.class));
    }

    @Test
    public void sampleControllerShouldDeleteASampleWhenItExists() throws SampleNotFoundException {
        Sample sample1 = new Sample(1L, "Juan");

        doNothing().when(sampleService).deleteSample(anyLong());

        sampleController.deleteSample(1L);

        verify(sampleService).deleteSample(1L);
    }

    @Test
    public void sampleControllerShouldNotDeleteASampleWhenItDoesNotExists() throws SampleNotFoundException {
        doThrow(new SampleNotFoundException()).when(sampleService).deleteSample(anyLong());

        thrown.expect(SampleNotFoundException.class);
        thrown.expectMessage(SampleNotFoundException.ERROR_MESSAGE);
        sampleController.deleteSample(1L);

        verify(sampleService).deleteSample(1L);
    }

    @Test
    public void sampleControllerShouldHandleSampleNotFoundException() {
        ResponseEntity<Object> result = sampleController.sampleNotFoundException();

        assertEquals(result.getBody(), SampleNotFoundException.ERROR_MESSAGE);
        assertEquals(result.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void sampleControllerShouldHandleSampleAlreadyCreatedException() {
        ResponseEntity<Object> result = sampleController.sampleAlreadyCreatedException();

        assertEquals(result.getBody(), SampleAlreadyCreatedException.ERROR_MESSAGE);
        assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void sampleControllerShouldHandleSampleBadRequestException() {
        ResponseEntity<Object> result = sampleController.sampleBadRequestException();

        assertEquals(result.getBody(), SampleBadRequestException.ERROR_MESSAGE);
        assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}