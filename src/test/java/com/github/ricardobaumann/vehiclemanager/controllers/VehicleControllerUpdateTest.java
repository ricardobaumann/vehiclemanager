package com.github.ricardobaumann.vehiclemanager.controllers;

import com.github.ricardobaumann.vehiclemanager.IntegrationTest;
import com.github.ricardobaumann.vehiclemanager.TestObjects;
import com.github.ricardobaumann.vehiclemanager.entities.FuelType;
import com.github.ricardobaumann.vehiclemanager.entities.Vehicle;
import com.github.ricardobaumann.vehiclemanager.repos.VehicleRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class VehicleControllerUpdateTest {

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
    void shouldUpdateValidVehicle() throws Exception {
        Vehicle vehicle = testObjects.createTestVehicle();

        mockMvc.perform(put("/v1/vehicles/{id}", vehicle.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("""
                        {
                            "modelId": "%s",
                            "year": 2001,
                            "fuelType": "ELECTRIC",
                            "doorsAmount": 3,
                            "color": "blue"
                        }
                        """, vehicle.getModel().getId()))
        ).andExpect(status().isOk());

        assertThat(vehicleRepo.findById(vehicle.getId()))
                .isPresent()
                .get()
                .hasFieldOrPropertyWithValue("year", 2001)
                .hasFieldOrPropertyWithValue("fuelType", FuelType.ELECTRIC)
                .hasFieldOrPropertyWithValue("doorsAmount", 3)
                .hasFieldOrPropertyWithValue("color", "blue")
        ;

    }

    @Test
    void shouldReturnBadRequestOnInvalidVehicle() throws Exception {
        Vehicle vehicle = testObjects.createTestVehicle();

        mockMvc.perform(put("/v1/vehicles/{id}", vehicle.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {}
                                """)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {"status":400,"message":"validation error","fieldErrors":[
                                    {"objectName":"createVehicleCommand","path":"modelId","message":"must not be null"},
                                    {"objectName":"createVehicleCommand","path":"year","message":"must not be null"},
                                    {"objectName":"createVehicleCommand","path":"fuelType","message":"must not be null"},
                                    {"objectName":"createVehicleCommand","path":"color","message":"must not be blank"},
                                    {"objectName":"createVehicleCommand","path":"doorsAmount","message":"must not be null"}]}
                        """));
    }

    @Test
    void shouldReturnBadRequestOnInvalidModel() throws Exception {
        mockMvc.perform(put("/v1/vehicles/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {
                                    "modelId": "%s",
                                    "year": 2001,
                                    "fuelType": "ELECTRIC",
                                    "doorsAmount": 3,
                                    "color": "blue"
                                }
                                """, UUID.randomUUID()))
                ).andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {"status":400,"message":"Model not found"}
                        """))
        ;
    }
}