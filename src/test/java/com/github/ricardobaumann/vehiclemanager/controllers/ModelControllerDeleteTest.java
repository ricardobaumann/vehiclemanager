package com.github.ricardobaumann.vehiclemanager.controllers;

import com.github.ricardobaumann.vehiclemanager.IntegrationTest;
import com.github.ricardobaumann.vehiclemanager.TestObjects;
import com.github.ricardobaumann.vehiclemanager.entities.Model;
import com.github.ricardobaumann.vehiclemanager.repos.ModelRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class ModelControllerDeleteTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestObjects testObjects;
    @Autowired
    private ModelRepo modelRepo;

    @Test
    void shouldDeleteExistentModel() throws Exception {
        Model model = testObjects.createTestModel();

        mockMvc.perform(delete("/v1/models/{id}", model.getId()))
                .andExpect(status().isOk());

        assertThat(modelRepo.existsById(model.getId()))
                .isFalse();
    }
}