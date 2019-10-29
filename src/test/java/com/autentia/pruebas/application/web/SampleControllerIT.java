package com.autentia.pruebas.application.web;

import com.autentia.pruebas.application.exceptions.SampleAlreadyCreatedException;
import com.autentia.pruebas.application.exceptions.SampleNotFoundException;
import com.autentia.pruebas.application.model.Sample;
import com.autentia.pruebas.application.service.SampleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SampleControllerIT {
    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private SampleService sampleService;

    private Sample sample1 = new Sample(1L, "Juan");
    private Sample sample2 = new Sample(2L, "Ana");
    private Sample newSample = new Sample(3L, "Alex");


    @Test
    public void returnsOKAndAllSamplesWhenYouRequestAllSamples() throws Exception {
        Page<Sample> expectedSamples = new PageImpl<>(List.of(sample1, sample2));

        when(sampleService.getAllSamples(any(Pageable.class))).thenReturn(expectedSamples);

        mvc.perform(get("/application/getAll").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(expectedSamples))));

        verify(sampleService).getAllSamples(any(Pageable.class));
    }

    @Test
    public void returnsERRORWhenYouRequestAllSamplesWithoutGet() throws Exception {
        mvc.perform(post("/application/getAll").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(put("/application/getAll").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(delete("/application/getAll").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void returnsOKAndSample1WhenYouRequestSample1() throws Exception {
        when(sampleService.getSampleById(anyLong())).thenReturn(sample1);

        mvc.perform(get("/application/get/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(sample1))));

        verify(sampleService).getSampleById(anyLong());
    }

    @Test
    public void returnsERRORWhenYouRequestSampleOutOfBounds() throws Exception {
        when(sampleService.getSampleById(anyLong())).thenThrow(new SampleNotFoundException());

        mvc.perform(get("/application/get/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(SampleNotFoundException.ERROR_MESSAGE)));

        verify(sampleService).getSampleById(anyLong());
    }

    @Test
    public void returnsERRORWhenYouRequestSampleWithoutGet() throws Exception {
        mvc.perform(post("/application/get/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(put("/application/get/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(delete("/application/get/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void returnsOKAndSample3WhenYouAddSample3() throws Exception {
        when(sampleService.addSample(newSample)).thenReturn(newSample);

        mvc.perform(post("/application/add").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newSample)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(newSample))));

        verify(sampleService).addSample(newSample);
    }

    @Test
    public void returnsERRORWhenYouWhenYouAddSampleAlreadyInDb() throws Exception {
        when(sampleService.addSample(sample1)).thenThrow(new SampleAlreadyCreatedException());

        mvc.perform(post("/application/add").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sample1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(SampleAlreadyCreatedException.ERROR_MESSAGE)));

        verify(sampleService).addSample(sample1);
    }

    @Test
    public void returnsERRORWhenYouAddSampleWithoutPost() throws Exception {
        mvc.perform(get("/application/add").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newSample)))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(put("/application/add").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newSample)))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(delete("/application/add").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newSample)))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void returnsOKAndSample1WhenYouEditSample1() throws Exception {
        String newName = "Alex";
        Sample updatedSample = new Sample(1L, newName);

        when(sampleService.updateSample(anyLong(), anyString())).thenReturn(updatedSample);

        mvc.perform(put("/application/update/1").contentType(MediaType.APPLICATION_JSON)
                .content(newName))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(updatedSample))));

        verify(sampleService).updateSample(anyLong(), anyString());
    }

    @Test
    public void returnsERRORWhenYouWhenYouEditSampleNotInDb() throws Exception {
        String newName = "Alex";

        when(sampleService.updateSample(anyLong(), anyString())).thenThrow(new SampleNotFoundException());

        mvc.perform(put("/application/update/3").contentType(MediaType.APPLICATION_JSON)
                .content(newName))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(SampleNotFoundException.ERROR_MESSAGE)));

        verify(sampleService).updateSample(anyLong(), anyString());
    }

    @Test
    public void returnsERRORWhenYouEditSampleWithoutPut() throws Exception {
        String newName = "Ana";

        mvc.perform(get("/application/update/1").contentType(MediaType.APPLICATION_JSON).content(newName))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(post("/application/update/1").contentType(MediaType.APPLICATION_JSON).content(newName))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(delete("/application/update/1").contentType(MediaType.APPLICATION_JSON).content(newName))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void returnsOKAndSample1WhenYouDeleteSample1() throws Exception {
        when(sampleService.deleteSample(anyLong())).thenReturn(sample1);

        mvc.perform(delete("/application/delete/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(sample1))));

        verify(sampleService).deleteSample(anyLong());
    }

    @Test
    public void returnsERRORWhenYouWhenYouDeleteSampleNotInDb() throws Exception {
        when(sampleService.deleteSample(anyLong())).thenThrow(new SampleNotFoundException());

        mvc.perform(delete("/application/delete/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(SampleNotFoundException.ERROR_MESSAGE)));

        verify(sampleService).deleteSample(anyLong());
    }

    @Test
    public void returnsERRORWhenYouDeleteSampleWithoutDel() throws Exception {
        mvc.perform(get("/application/delete/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(post("/application/delete/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(put("/application/delete/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }
}
