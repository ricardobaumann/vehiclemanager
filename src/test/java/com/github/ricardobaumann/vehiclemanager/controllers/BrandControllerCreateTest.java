package com.github.ricardobaumann.vehiclemanager.controllers;

import com.github.ricardobaumann.vehiclemanager.IntegrationTest;
import com.github.ricardobaumann.vehiclemanager.entities.Brand;
import com.github.ricardobaumann.vehiclemanager.repos.BrandRepo;
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
class BrandControllerCreateTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrandRepo brandRepo;

    @BeforeEach
    void setUp() {
        brandRepo.deleteAll();
    }

    @Test
    void shouldReturnCreatedOnValidBrand() throws Exception {
        long count = brandRepo.count();
        mockMvc.perform(post("/v1/brands")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "foobar"
                                }
                                """)
                ).andExpect(status().isCreated())
                .andExpect(header().exists("location"));
        assertThat(brandRepo.count()).isEqualTo(count + 1);
    }

    @Test
    void shouldReturnBadRequestOnInvalidBrand() throws Exception {
        mockMvc.perform(post("/v1/brands")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": ""
                                }
                                """)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {"status":400,"message":"validation error","fieldErrors":[
                        {"objectName":"createBrandCommand","path":"name","message":"size must be between 5 and 100"},
                        {"objectName":"createBrandCommand","path":"name","message":"must not be blank"}]}
                        """))
        ;
    }

    @Test
    void shouldReturnBadRequestOnDuplicatedBrand() throws Exception {
        Brand entity = new Brand();
        entity.setName("thebrand");
        entity.setId(UUID.randomUUID());

        brandRepo.save(entity);

        mockMvc.perform(post("/v1/brands")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "thebrand"
                                }
                                """)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {"status":400,"message":"Duplicated brand name"}
                        """))
        ;
    }
}