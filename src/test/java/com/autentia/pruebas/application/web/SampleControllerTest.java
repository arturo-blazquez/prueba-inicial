package com.autentia.pruebas.application.web;

import com.autentia.pruebas.application.model.Sample;
import com.autentia.pruebas.application.service.SampleService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.data.domain.*;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class SampleControllerTest {

    private SampleController sampleController;
    private SampleService sampleService;

    @Before
    public void init() {
        sampleService = mock(SampleService.class);
        sampleController = new SampleController(sampleService);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void sample_controller_should_get_all_samples_when_some_exist() {
        Sample sample1 = new Sample(1L, "Juan");
        Sample sample2 = new Sample(2L, "Ana");
        Page<Sample> expectedSamples = new PageImpl<>(List.of(sample1, sample2));
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by("name").descending());

        when(sampleService.getAllSamples(pageRequest)).thenReturn(expectedSamples);

        Page<Sample> samplesFound = sampleController.getAllSamples(pageRequest);

        verify(sampleService).getAllSamples(pageRequest);
        assertEquals(samplesFound, expectedSamples);
    }

    @Test
    public void sample_controller_should_get_no_samples_when_there_are_none() {
        Page<Sample> emptySamples = Page.empty();
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by("name").descending());

        when(sampleService.getAllSamples(pageRequest)).thenReturn(emptySamples);

        Page<Sample> samplesFound = sampleController.getAllSamples(pageRequest);

        verify(sampleService).getAllSamples(pageRequest);
        assertEquals(samplesFound, emptySamples);
    }

    @Test
    public void sample_controller_should_get_sample_when_id_exists() {
        Sample sample1 = new Sample(1L, "Juan");

        when(sampleService.getSampleById(1L)).thenReturn(sample1);

        Sample sampleFound = sampleController.getSampleById(1L);

        verify(sampleService).getSampleById(1L);
        assertEquals(sampleFound, sample1);
    }

    @Test
    public void sample_controller_should_get_no_samples_when_id_does_not_exist() {
        when(sampleService.getSampleById(1L)).thenThrow(new RuntimeException("Sample no encontrado"));

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Sample no encontrado");
        Sample sampleFound = sampleController.getSampleById(1L);

        verify(sampleService).getSampleById(1L);
    }

    @Test
    public void sample_controller_should_add_a_new_sample_when_its_doesnt_already_exist() {
        Sample sample1 = new Sample(1L, "Juan");

        when(sampleService.addSample(sample1)).thenReturn(sample1);

        Sample sampleAdded = sampleController.addSample(sample1);

        verify(sampleService).addSample(sample1);
        assertEquals(sampleAdded, sample1);
    }

    @Test
    public void sample_controller_should_not_add_a_new_sample_when_it_already_exists() {
        Sample sample1 = new Sample(1L, "Juan");

        when(sampleService.addSample(sample1)).thenThrow(new RuntimeException("Sample ya en la base de datos"));

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Sample ya en la base de datos");
        Sample sampleFound = sampleController.addSample(sample1);

        verify(sampleService).addSample(sample1);
    }

    @Test
    public void sample_controller_should_update_a_sample_when_it_exists() {
        String newName = "Ana";
        Sample sample2 = new Sample(1L, newName);

        when(sampleService.updateSample(1L, newName)).thenReturn(sample2);

        Sample sampleUpdated = sampleController.updateSample(1L, newName);

        verify(sampleService).updateSample(1L, newName);
        assertEquals(sampleUpdated, sample2);
    }

    @Test
    public void sample_controller_should_not_update_a_sample_when_it_does_not_exists() {
        String newName = "Ana";

        when(sampleService.updateSample(1L, newName)).thenThrow(new RuntimeException("Sample no existe en la base de datos"));

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Sample no existe en la base de datos");
        Sample sampleFound = sampleController.updateSample(1L, newName);

        verify(sampleService).updateSample(1L, newName);
    }

    @Test
    public void sample_controller_should_delete_a_sample_when_it_exists() {
        Sample sample1 = new Sample(1L, "Juan");

        when(sampleService.deleteSample(1L)).thenReturn(sample1);

        Sample sampleDeleted = sampleController.deleteSample(1L);

        verify(sampleService).deleteSample(1L);
        assertEquals(sampleDeleted, sample1);
    }

    @Test
    public void sample_controller_should_not_delete_a_sample_when_it_does_not_exists() {
        when(sampleService.deleteSample(1L)).thenThrow(new RuntimeException("Sample no existe en la base de datos"));

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Sample no existe en la base de datos");
        Sample sampleDeleted = sampleController.deleteSample(1L);

        verify(sampleService).deleteSample(1L);
    }
}