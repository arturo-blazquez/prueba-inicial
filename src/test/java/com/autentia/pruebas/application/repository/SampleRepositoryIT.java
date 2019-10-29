package com.autentia.pruebas.application.repository;

import com.autentia.pruebas.application.model.Sample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SampleRepositoryIT {
    @Autowired
    private SampleRepository sampleRepository;

    @Test
    public void ShouldFindAllSamples() {
        Sample sample1 = new Sample(1L, "Juan");
        Sample sample2 = new Sample(2L, "Ana");
        List<Sample> expectedSamples = List.of(sample1, sample2);

        Iterable<Sample> allSamples = sampleRepository.findAll();

        assertEquals(allSamples, expectedSamples);
    }

    @Test
    public void whenGEtByValidIdYouShouldGetAValidResult() {
        Sample foundSample1 = sampleRepository.findById(1L).get();
        Sample foundSample2 = sampleRepository.findById(2L).get();

        assertEquals(foundSample1.getName(), "Juan");
        assertEquals(foundSample2.getName(), "Ana");
    }

    @Test(expected = NoSuchElementException.class)
    public void whenGEtByWrongIdYouShouldGetNoResults() {
        sampleRepository.findById(3L).get();
    }

    @Test
    public void shouldAddANewSample() {
        Sample sample3 = new Sample(3L, "Alex");

        Sample foundSample = sampleRepository.save(sample3);

        assertEquals(foundSample, sample3);
    }

    @Test
    public void shouldUpdateSampleAlreadyInDb() {
        Sample sample1 = new Sample(1L, "Alex");

        Sample foundSample = sampleRepository.save(sample1);

        assertEquals(foundSample, sample1);
    }
}