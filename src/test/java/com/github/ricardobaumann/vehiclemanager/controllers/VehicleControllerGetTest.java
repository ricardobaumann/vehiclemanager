package com.github.ricardobaumann.vehiclemanager.controllers;

import com.github.ricardobaumann.vehiclemanager.IntegrationTest;
import com.github.ricardobaumann.vehiclemanager.TestObjects;
import com.github.ricardobaumann.vehiclemanager.entities.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class VehicleControllerGetTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestObjects testObjects;

    @BeforeEach
    void setUp() {
        testObjects.cleanUp();
    }

    @Test
    void shouldReturnExistentVehicle() throws Exception {
        Vehicle vehicle = testObjects.createTestVehicle();

        mockMvc.perform(get("/v1/vehicles/{id}", vehicle.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(vehicle.getId().toString())))
                .andExpect(jsonPath("$.model.name", is(vehicle.getModel().getName())))
                .andExpect(jsonPath("$.model.brand.name", is(vehicle.getModel().getBrand().getName())))
                .andExpect(jsonPath("$.doorsAmount", is(vehicle.getDoorsAmount())))
                .andExpect(jsonPath("$.year", is(vehicle.getYear())))
                .andExpect(jsonPath("$.color", is(vehicle.getColor())))
                .andExpect(jsonPath("$.fuelType", is(vehicle.getFuelType().toString())))
        ;
    }

    @Test
    void shouldReturnNotFoundOnNonExistentVehicle() throws Exception {
        mockMvc.perform(get("/v1/vehicles/{id}", UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
        ;
    }
}