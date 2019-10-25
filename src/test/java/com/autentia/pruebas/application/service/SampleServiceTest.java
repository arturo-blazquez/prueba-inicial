package com.autentia.pruebas.application.service;

import com.autentia.pruebas.application.model.Sample;
import com.autentia.pruebas.application.repository.SampleRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class SampleServiceTest {
    private SampleService sampleService;
    private SampleRepository sampleRepository;

    @Before
    public void init() {
        sampleRepository = mock(SampleRepository.class);
        sampleService = new SampleService(sampleRepository);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void sample_service_should_get_all_samples_when_some_exist() {
        Sample sample1 = new Sample(1L, "Juan");
        Sample sample2 = new Sample(2L, "Ana");
        Page<Sample> expectedSamples = new PageImpl<>(List.of(sample1, sample2));
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by("name").descending());

        when(sampleRepository.findAll(pageRequest)).thenReturn(expectedSamples);

        Page<Sample> samplesFound = sampleService.getAllSamples(pageRequest);

        verify(sampleRepository).findAll(pageRequest);
        assertEquals(samplesFound, expectedSamples);
    }

    @Test
    public void sample_service_should_get_no_samples_when_there_are_none() {
        Page<Sample> emptySamples = Page.empty();
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by("name").descending());

        when(sampleRepository.findAll(pageRequest)).thenReturn(emptySamples);

        Page<Sample> samplesFound = sampleService.getAllSamples(pageRequest);

        verify(sampleRepository).findAll(pageRequest);
        assertEquals(samplesFound, emptySamples);
    }

    @Test
    public void sample_service_should_get_sample_when_id_exists() {
        Sample sample1 = new Sample(1L, "Juan");
        Optional<Sample> optionalSample = Optional.of(sample1);

        when(sampleRepository.findById(Mockito.anyLong())).thenReturn(optionalSample);

        Sample sampleFound = sampleService.getSampleById(1L);

        verify(sampleRepository).findById(Mockito.anyLong());
        assertEquals(sampleFound, sample1);
    }

    @Test
    public void sample_service_should_get_no_samples_when_id_does_not_exist() {
        Optional<Sample> emptyOptionalSample = Optional.empty();

        when(sampleRepository.findById(Mockito.anyLong())).thenReturn(emptyOptionalSample);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Sample no encontrado");
        Sample sampleFound = sampleService.getSampleById(1L);

        verify(sampleRepository).findById(Mockito.anyLong());
    }

    @Test
    public void sample_service_should_add_a_new_sample_when_it_doesnt_already_exist() {
        Sample sample1 = new Sample(1L, "Juan");
        Optional<Sample> emptyOptionalSample = Optional.empty();

        when(sampleRepository.findById(Mockito.anyLong())).thenReturn(emptyOptionalSample);
        when(sampleRepository.save(sample1)).thenReturn(sample1);

        Sample sampleAdded = sampleService.addSample(sample1);

        verify(sampleRepository).findById(Mockito.anyLong());
        verify(sampleRepository).save(sample1);
        assertEquals(sampleAdded, sample1);
    }

    @Test
    public void sample_service_should_not_add_a_new_sample_when_it_already_exists() {
        Sample sample1 = new Sample(1L, "Juan");
        Optional<Sample> optionalSample = Optional.of(sample1);

        when(sampleRepository.findById(Mockito.anyLong())).thenReturn(optionalSample);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Sample ya en la base de datos");
        Sample sampleFound = sampleService.addSample(sample1);

        verify(sampleRepository).findById(Mockito.anyLong());
    }

    @Test
    public void sample_service_should_update_a_sample_when_it_exists() {
        String newName = "Ana";
        Sample sample1 = new Sample(1L, "Juan");
        Sample sample2 = new Sample(1L, newName);
        Optional<Sample> optionalSample = Optional.of(sample1);

        when(sampleRepository.findById(Mockito.anyLong())).thenReturn(optionalSample);
        when(sampleRepository.save(sample2)).thenReturn(sample2);

        Sample sampleUpdated = sampleService.updateSample(1L, newName);

        verify(sampleRepository).findById(Mockito.anyLong());
        verify(sampleRepository).save(sample2);
        assertEquals(sampleUpdated, sample2);
    }

    @Test
    public void sample_service_should_not_update_a_sample_when_it_does_not_exists() {
        String newName = "Ana";
        Optional<Sample> emptyOptionalSample = Optional.empty();

        when(sampleRepository.findById(Mockito.anyLong())).thenReturn(emptyOptionalSample);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Sample no existe en la base de datos");
        Sample sampleFound = sampleService.updateSample(1L, newName);

        verify(sampleRepository).findById(Mockito.anyLong());
    }

    @Test
    public void sample_service_should_delete_a_sample_when_it_exists() {
        Sample sample1 = new Sample(1L, "Juan");
        Optional<Sample> optionalSample = Optional.of(sample1);

        when(sampleRepository.findById(Mockito.anyLong())).thenReturn(optionalSample);

        Sample sampleDeleted = sampleService.deleteSample(1L);

        verify(sampleRepository).findById(Mockito.anyLong());
        verify(sampleRepository).delete(sample1);
        assertEquals(sampleDeleted, sample1);
    }

    @Test
    public void sample_service_should_not_delete_a_sample_when_it_does_not_exists() {
        Optional<Sample> emptyOptionalSample = Optional.empty();

        when(sampleRepository.findById(Mockito.anyLong())).thenReturn(emptyOptionalSample);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Sample no existe en la base de datos");
        Sample sampleDeleted = sampleService.deleteSample(1L);

        verify(sampleRepository).findById(Mockito.anyLong());
    }
}