package com.autentia.pruebas.application.service;

import com.autentia.pruebas.application.exceptions.SampleAlreadyCreatedException;
import com.autentia.pruebas.application.exceptions.SampleNotFoundException;
import com.autentia.pruebas.application.model.Sample;
import com.autentia.pruebas.application.repository.SampleRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class SampleServiceTest {
    private SampleService sampleService;
    private SampleRepository sampleRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void init() {
        sampleRepository = mock(SampleRepository.class);
        sampleService = new SampleService(sampleRepository);
    }

    @Test
    public void sampleServiceShouldGetAllSamplesWhenSomeExist() {
        Sample sample1 = new Sample(1L, "Juan");
        Sample sample2 = new Sample(2L, "Ana");
        Page<Sample> expectedSamples = new PageImpl<>(List.of(sample1, sample2));
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by("name").descending());

        when(sampleRepository.findAll(any(Pageable.class))).thenReturn(expectedSamples);

        Page<Sample> samplesFound = sampleService.getAllSamples(pageRequest);

        verify(sampleRepository).findAll(any(Pageable.class));
        assertEquals(samplesFound, expectedSamples);
    }

    @Test
    public void sampleServiceShouldGetNoSamplesWhenThereAreNone() {
        Page<Sample> emptySamples = Page.empty();
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by("name").descending());

        when(sampleRepository.findAll(any(Pageable.class))).thenReturn(emptySamples);

        Page<Sample> samplesFound = sampleService.getAllSamples(pageRequest);

        verify(sampleRepository).findAll(any(Pageable.class));
        assertEquals(samplesFound, emptySamples);
    }

    @Test
    public void sampleServiceShouldGetSampleWhenIdExists() throws SampleNotFoundException {
        Sample sample1 = new Sample(1L, "Juan");
        Optional<Sample> optionalSample = Optional.of(sample1);

        when(sampleRepository.findById(anyLong())).thenReturn(optionalSample);

        Sample sampleFound = sampleService.getSampleById(1L);

        verify(sampleRepository).findById(anyLong());
        assertEquals(sampleFound, sample1);
    }

    @Test
    public void sampleServiceShouldGetNoSamplesWhenIdDoesNotExist() throws SampleNotFoundException {
        Optional<Sample> emptyOptionalSample = Optional.empty();

        when(sampleRepository.findById(anyLong())).thenReturn(emptyOptionalSample);

        thrown.expect(SampleNotFoundException.class);
        thrown.expectMessage(SampleNotFoundException.ERROR_MESSAGE);
        sampleService.getSampleById(1L);

        verify(sampleRepository).findById(anyLong());
    }

    @Test
    public void sampleServiceShouldAddANewSampleWhenItDoesntAlreadyExist() throws SampleAlreadyCreatedException {
        Sample sample1 = new Sample(1L, "Juan");
        Optional<Sample> emptyOptionalSample = Optional.empty();

        when(sampleRepository.findById(anyLong())).thenReturn(emptyOptionalSample);
        when(sampleRepository.save(sample1)).thenReturn(sample1);

        Sample sampleAdded = sampleService.addSample(sample1);

        verify(sampleRepository).findById(anyLong());
        verify(sampleRepository).save(sample1);
        assertEquals(sampleAdded, sample1);
    }

    @Test
    public void sampleServiceShouldNotAddANewSampleWhenItAlreadyExists() throws SampleAlreadyCreatedException {
        Sample sample1 = new Sample(1L, "Juan");
        Optional<Sample> optionalSample = Optional.of(sample1);

        when(sampleRepository.findById(anyLong())).thenReturn(optionalSample);

        thrown.expect(SampleAlreadyCreatedException.class);
        thrown.expectMessage(SampleAlreadyCreatedException.ERROR_MESSAGE);
        sampleService.addSample(sample1);

        verify(sampleRepository).findById(anyLong());
    }

    @Test
    public void sampleServiceShouldUpdateASampleWhenItExists() throws SampleNotFoundException {
        String newName = "Ana";
        Sample sample1 = new Sample(1L, "Juan");
        Sample sample2 = new Sample(1L, newName);
        Optional<Sample> optionalSample = Optional.of(sample1);

        when(sampleRepository.findById(anyLong())).thenReturn(optionalSample);
        when(sampleRepository.save(sample2)).thenReturn(sample2);

        Sample sampleUpdated = sampleService.updateSample(1L, newName);

        verify(sampleRepository).findById(anyLong());
        verify(sampleRepository).save(sample2);
        assertEquals(sampleUpdated, sample2);
    }

    @Test
    public void sampleServiceShouldNotUpdateASampleWhenItDoesNotExists() throws SampleNotFoundException {
        String newName = "Ana";
        Optional<Sample> emptyOptionalSample = Optional.empty();

        when(sampleRepository.findById(anyLong())).thenReturn(emptyOptionalSample);

        thrown.expect(SampleNotFoundException.class);
        thrown.expectMessage(SampleNotFoundException.ERROR_MESSAGE);
        sampleService.updateSample(1L, newName);

        verify(sampleRepository).findById(anyLong());
    }

    @Test
    public void sampleServiceShouldDeleteASampleWhenItExists() throws SampleNotFoundException {
        Sample sample1 = new Sample(1L, "Juan");
        Optional<Sample> optionalSample = Optional.of(sample1);

        when(sampleRepository.findById(anyLong())).thenReturn(optionalSample);

        Sample sampleDeleted = sampleService.deleteSample(1L);

        verify(sampleRepository).findById(anyLong());
        verify(sampleRepository).delete(sample1);
        assertEquals(sampleDeleted, sample1);
    }

    @Test
    public void sampleServiceShouldNotDeleteASampleWhenItDoesNotExists() throws SampleNotFoundException {
        Optional<Sample> emptyOptionalSample = Optional.empty();

        when(sampleRepository.findById(anyLong())).thenReturn(emptyOptionalSample);

        thrown.expect(SampleNotFoundException.class);
        thrown.expectMessage(SampleNotFoundException.ERROR_MESSAGE);
        sampleService.deleteSample(1L);

        verify(sampleRepository).findById(anyLong());
    }
}