package com.github.ricardobaumann.vehiclemanager.controllers;

import com.github.ricardobaumann.vehiclemanager.IntegrationTest;
import com.github.ricardobaumann.vehiclemanager.TestObjects;
import com.github.ricardobaumann.vehiclemanager.entities.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class VehicleControllerListTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestObjects testObjects;

    @BeforeEach
    void setUp() {
        testObjects.cleanUp();
    }

    @Test
    void shouldListVehicles() throws Exception {
        Vehicle vehicle = testObjects.createTestVehicle();
        
        mockMvc.perform(get("/v1/vehicles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.content.[0].id", is(vehicle.getId().toString())))
                .andExpect(jsonPath("$.content.[0].year", is(vehicle.getYear())))
                .andExpect(jsonPath("$.content.[0].color", is(vehicle.getColor())))
                .andExpect(jsonPath("$.content.[0].doorsAmount", is(vehicle.getDoorsAmount())))
                .andExpect(jsonPath("$.content.[0].model.name", is(vehicle.getModel().getName())))
        ;
    }
}