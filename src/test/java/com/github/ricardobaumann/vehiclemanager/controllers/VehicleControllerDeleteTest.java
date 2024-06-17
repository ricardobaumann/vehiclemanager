package com.github.ricardobaumann.vehiclemanager.controllers;

import com.github.ricardobaumann.vehiclemanager.IntegrationTest;
import com.github.ricardobaumann.vehiclemanager.TestObjects;
import com.github.ricardobaumann.vehiclemanager.entities.Vehicle;
import com.github.ricardobaumann.vehiclemanager.repos.VehicleRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class VehicleControllerDeleteTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestObjects testObjects;
    @Autowired
    private VehicleRepo vehicleRepo;

    @BeforeEach
    void setUp() {
        testObjects.cleanUp();
    }

    @Test
    void shouldDeleteExistentVehicle() throws Exception {
        Vehicle vehicle = testObjects.createTestVehicle();

        mockMvc.perform(delete("/v1/vehicles/{id}", vehicle.getId()))
                .andExpect(status().isOk());

        assertThat(vehicleRepo.count()).isZero();
    }
}