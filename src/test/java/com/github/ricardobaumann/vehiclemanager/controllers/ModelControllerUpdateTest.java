package com.github.ricardobaumann.vehiclemanager.controllers;

import com.github.ricardobaumann.vehiclemanager.IntegrationTest;
import com.github.ricardobaumann.vehiclemanager.entities.Brand;
import com.github.ricardobaumann.vehiclemanager.entities.Model;
import com.github.ricardobaumann.vehiclemanager.repos.BrandRepo;
import com.github.ricardobaumann.vehiclemanager.repos.ModelRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class ModelControllerUpdateTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelRepo modelRepo;
    @Autowired
    private BrandRepo brandRepo;

    @Test
    void shouldUpdateValidModel() throws Exception {
        Brand brand = brandRepo.save(new Brand(UUID.randomUUID(), "brandfoo"));
        Model model = new Model(UUID.randomUUID(), brand, "testing", new BigDecimal("100.99"));
        modelRepo.save(model);

        mockMvc.perform(put("/v1/models/{id}", model.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("""
                        {
                            "brandId": "%s",
                            "name": "foobar",
                            "price": 100.2
                        }
                        """, brand.getId()))
        ).andExpect(status().isOk());

        assertThat(modelRepo.findById(model.getId()))
                .isPresent()
                .get()
                .hasFieldOrPropertyWithValue("name", "foobar")
                .hasFieldOrPropertyWithValue("price", new BigDecimal("100.200"))
        ;
    }

    @Test
    void shouldReturnBadRequestOnInvalidInput() throws Exception {
        Brand brand = brandRepo.save(new Brand(UUID.randomUUID(), "brandfoo"));
        Model model = new Model(UUID.randomUUID(), brand, "testing", new BigDecimal("100.99"));
        modelRepo.save(model);

        mockMvc.perform(put("/v1/models/{id}", model.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {}
                                """)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {"status":400,"message":"validation error",
                        "fieldErrors":[
                            {"objectName":"createModelCommand","path":"name","message":"must not be blank"},
                            {"objectName":"createModelCommand","path":"price","message":"must not be null"},
                            {"objectName":"createModelCommand","path":"brandId","message":"must not be null"}]}
                        """));
    }

}