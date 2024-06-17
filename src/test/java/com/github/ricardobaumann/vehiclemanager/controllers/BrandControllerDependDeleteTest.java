package com.github.ricardobaumann.vehiclemanager.controllers;

import com.github.ricardobaumann.vehiclemanager.IntegrationTest;
import com.github.ricardobaumann.vehiclemanager.TestObjects;
import com.github.ricardobaumann.vehiclemanager.entities.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class BrandControllerDependDeleteTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestObjects testObjects;

    @BeforeEach
    void setUp() {
        testObjects.cleanUp();
    }

    @Test
    @DisplayName("""
            it should return bad request when trying to delete a brand
            still linked to a model
            """)
    void shouldReturnBadRequestOnDepDelete() throws Exception {
        Model model = testObjects.createTestModel();

        mockMvc.perform(delete("/v1/brands/{id}", model.getBrand().getId()))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {"status":400,"message":"The brand is still linked to a model"}
                        """));
    }

}