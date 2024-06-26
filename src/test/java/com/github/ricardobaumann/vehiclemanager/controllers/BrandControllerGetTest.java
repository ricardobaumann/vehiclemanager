package com.github.ricardobaumann.vehiclemanager.controllers;

import com.github.ricardobaumann.vehiclemanager.IntegrationTest;
import com.github.ricardobaumann.vehiclemanager.TestObjects;
import com.github.ricardobaumann.vehiclemanager.entities.Brand;
import com.github.ricardobaumann.vehiclemanager.repos.BrandRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class BrandControllerGetTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BrandRepo brandRepo;
    @Autowired
    private TestObjects testObjects;

    private final UUID id = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        testObjects.cleanUp();
        Brand entity = new Brand();
        entity.setId(id);
        entity.setName("thebrand");
        brandRepo.save(entity);
    }

    @Test
    void shouldReturnExistentBrand() throws Exception {
        mockMvc.perform(get("/v1/brands/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(
                        String.format("""
                                {"id":"%s","name":"thebrand"}
                                """, id)));
    }

    @Test
    void shouldReturnNotFoundOnNonExistentBrand() throws Exception {
        mockMvc.perform(get("/v1/brands/{id}", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }
}