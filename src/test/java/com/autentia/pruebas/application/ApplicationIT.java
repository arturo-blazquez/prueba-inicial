package com.autentia.pruebas.application;

import com.autentia.pruebas.application.exceptions.SampleAlreadyCreatedException;
import com.autentia.pruebas.application.exceptions.SampleNotFoundException;
import com.autentia.pruebas.application.model.Sample;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ApplicationIT {
    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Sample sample1 = new Sample(1L, "Juan");
    private Sample sample2 = new Sample(2L, "Ana");
    private Sample newSample = new Sample(3L, "Alex");


    @Test
    public void returnsOKAndAllSamplesWhenYouRequestAllSamples() throws Exception {
        List<Sample> expectedSamples = List.of(sample1, sample2);

        mvc.perform(get("/application/getAll").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(expectedSamples))));
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
        mvc.perform(get("/application/get/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(sample1))));
    }

    @Test
    public void returnsERRORWhenYouRequestSampleOutOfBounds() throws Exception {
        mvc.perform(get("/application/get/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(SampleNotFoundException.ERROR_MESSAGE)));
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
        mvc.perform(post("/application/add").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newSample)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(newSample))));
    }

    @Test
    public void returnsERRORWhenYouWhenYouAddSampleAlreadyInDb() throws Exception {
        mvc.perform(post("/application/add").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sample1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(SampleAlreadyCreatedException.ERROR_MESSAGE)));
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

        mvc.perform(put("/application/update/1").contentType(MediaType.APPLICATION_JSON)
                .content(newName))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(updatedSample))));
    }

    @Test
    public void returnsERRORWhenYouWhenYouEditSampleNotInDb() throws Exception {
        String newName = "Alex";

        mvc.perform(put("/application/update/3").contentType(MediaType.APPLICATION_JSON)
                .content(newName))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(SampleNotFoundException.ERROR_MESSAGE)));
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
        mvc.perform(delete("/application/delete/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(sample1))));
    }

    @Test
    public void returnsERRORWhenYouWhenYouDeleteSampleNotInDb() throws Exception {
        mvc.perform(delete("/application/delete/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(SampleNotFoundException.ERROR_MESSAGE)));
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