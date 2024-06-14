package com.github.ricardobaumann.vehiclemanager.controllers;

import com.github.ricardobaumann.vehiclemanager.IntegrationTest;
import com.github.ricardobaumann.vehiclemanager.TestObjects;
import com.github.ricardobaumann.vehiclemanager.entities.Brand;
import com.github.ricardobaumann.vehiclemanager.entities.Model;
import com.github.ricardobaumann.vehiclemanager.repos.BrandRepo;
import com.github.ricardobaumann.vehiclemanager.repos.ModelRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
class ModelControllerCreateTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelRepo modelRepo;
    @Autowired
    private BrandRepo brandRepo;
    @Autowired
    private TestObjects testObjects;

    @BeforeEach
    void setup() {
        testObjects.cleanUp();
    }

    @Test
    void shouldCreateValidModel() throws Exception {
        brandRepo.deleteAll();
        UUID brandId = brandRepo.save(new Brand(UUID.randomUUID(), "brandfoo")).getId();
        mockMvc.perform(post("/v1/models")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {
                                    "brandId": "%s",
                                    "name": "foobar",
                                    "price": 100.2
                                }
                                """, brandId))
                ).andExpect(status().isCreated())
                .andExpect(header().exists("location"));
        Model model = modelRepo.findAll().iterator().next();
        assertThat(model)
                .hasFieldOrPropertyWithValue("name", "foobar")
                .hasFieldOrPropertyWithValue("brand.id", brandId)
                .hasFieldOrPropertyWithValue("brand.name", "brandfoo")
        ;
        assertThat(model.getPrice().doubleValue()).isEqualTo(new BigDecimal("100.2").doubleValue());
    }

    @Test
    void shouldReturnBadRequestOnInvalidInput() throws Exception {
        mockMvc.perform(post("/v1/models")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                }
                                """)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {"status":400,"message":"validation error",
                            "fieldErrors":[
                                {"objectName":"createModelCommand","path":"price","message":"must not be null"},
                                {"objectName":"createModelCommand","path":"brandId","message":"must not be null"},
                                {"objectName":"createModelCommand","path":"name","message":"must not be blank"}]}
                        """))
        ;
    }

    @Test
    void shouldReturnBadRequestOnInvalidBrand() throws Exception {
        mockMvc.perform(post("/v1/models")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {
                                    "brandId": "%s",
                                    "name": "foobar",
                                    "price": 100.2
                                }
                                """, UUID.randomUUID()))
                ).andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {"status":400,"message":"Brand not found"}
                        """));
    }

    @Test
    void shouldReturnBadRequestOnDuplicatedModel() throws Exception {
        Brand brand = brandRepo.save(new Brand(UUID.randomUUID(), "brandfoo"));
        modelRepo.save(new Model(UUID.randomUUID(), brand, "testing", new BigDecimal("100.99")));

        mockMvc.perform(post("/v1/models")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {
                                    "brandId": "%s",
                                    "name": "testing",
                                    "price": 100.2
                                }
                                """, brand.getId()))
                ).andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {"status":400,"message":"Duplicated model"}
                        """));
    }
}