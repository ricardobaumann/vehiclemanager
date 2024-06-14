package com.github.ricardobaumann.vehiclemanager.controllers;

import com.github.ricardobaumann.vehiclemanager.IntegrationTest;
import com.github.ricardobaumann.vehiclemanager.TestObjects;
import com.github.ricardobaumann.vehiclemanager.entities.Model;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class ModelControllerGetTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestObjects testObjects;

    @Test
    void shouldReturnExistentModel() throws Exception {
        Model model = testObjects.createTestModel();

        mockMvc.perform(get("/v1/models/{id}", model.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(model.getId().toString())))
                .andExpect(jsonPath("$.name", is(model.getName())))
                .andExpect(jsonPath("$.brand.name", is(model.getBrand().getName())))
                .andExpect(jsonPath("$.price", is(closeTo(model.getPrice().doubleValue(), 0))))
        ;
    }

    @Test
    void shouldReturnNotFoundOnNonExistentModel() throws Exception {
        mockMvc.perform(get("/v1/models/{id}", UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }
}