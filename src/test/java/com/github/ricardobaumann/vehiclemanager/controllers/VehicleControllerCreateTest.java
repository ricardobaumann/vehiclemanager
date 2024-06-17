package com.github.ricardobaumann.vehiclemanager.controllers;

import com.github.ricardobaumann.vehiclemanager.IntegrationTest;
import com.github.ricardobaumann.vehiclemanager.TestObjects;
import com.github.ricardobaumann.vehiclemanager.entities.FuelType;
import com.github.ricardobaumann.vehiclemanager.entities.Model;
import com.github.ricardobaumann.vehiclemanager.repos.VehicleRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
class VehicleControllerCreateTest {

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
    void shouldCreateValidVehicle() throws Exception {
        Model model = testObjects.createTestModel();

        mockMvc.perform(post("/v1/vehicles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {
                                    "modelId": "%s",
                                    "year": 2000,
                                    "fuelType": "GASOLINE",
                                    "doorsAmount": 2,
                                    "color": "red"
                                }
                                """, model.getId()))
                ).andExpect(status().isCreated())
                .andExpect(header().exists("location"));
        assertThat(vehicleRepo.findAll().iterator().next())
                .hasFieldOrPropertyWithValue("model.id", model.getId())
                .hasFieldOrPropertyWithValue("model.name", model.getName())
                .hasFieldOrPropertyWithValue("year", 2000)
                .hasFieldOrPropertyWithValue("fuelType", FuelType.GASOLINE)
                .hasFieldOrPropertyWithValue("doorsAmount", 2)
                .hasFieldOrPropertyWithValue("color", "red")
        ;
    }

    @Test
    void shouldReturnBadRequestOnInvalidVehicle() throws Exception {
        mockMvc.perform(post("/v1/vehicles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {}
                                """)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {"status":400,"message":"validation error",
                                "fieldErrors":[
                                    {"objectName":"createVehicleCommand","path":"doorsAmount","message":"must not be null"},
                                    {"objectName":"createVehicleCommand","path":"modelId","message":"must not be null"},
                                    {"objectName":"createVehicleCommand","path":"year","message":"must not be null"},
                                    {"objectName":"createVehicleCommand","path":"color","message":"must not be blank"},
                                    {"objectName":"createVehicleCommand","path":"fuelType","message":"must not be null"}]}
                        """));
    }

    @Test
    void shouldReturnBadRequestOnInvalidInputModel() throws Exception {
        mockMvc.perform(post("/v1/vehicles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {
                                    "modelId": "%s",
                                    "year": 2000,
                                    "fuelType": "GASOLINE",
                                    "doorsAmount": 2,
                                    "color": "red"
                                }
                                """, UUID.randomUUID()))
                ).andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {"status":400,"message":"Model not found"}
                        """));
    }
}