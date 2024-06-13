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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class BrandControllerPutTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrandRepo brandRepo;

    private final UUID id = UUID.randomUUID();
    private final UUID id2 = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        brandRepo.deleteAll();
        Brand entity = new Brand();
        entity.setId(id);
        entity.setName("thebrand");
        brandRepo.save(entity);

        Brand entity2 = new Brand();
        entity2.setId(id2);
        entity2.setName("theother");
        brandRepo.save(entity2);
    }

    @Test
    void shouldUpdateExistentBrand() throws Exception {
        mockMvc.perform(put("/v1/brands/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "foobar"
                        }
                        """)
        ).andExpect(status().isOk());
        assertThat(brandRepo.findById(id))
                .isPresent()
                .get()
                .hasFieldOrPropertyWithValue("name", "foobar");
    }

    @Test
    void shouldValidateInput() throws Exception {
        mockMvc.perform(put("/v1/brands/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": ""
                                }
                                """)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json("""
                            {"status":400,"message":"validation error",
                                "fieldErrors":[
                                    {"objectName":"createBrandCommand","path":"name","message":"must not be blank"},
                                    {"objectName":"createBrandCommand","path":"name","message":"size must be between 5 and 100"}]}
                        """))
        ;
    }

    @Test
    void shouldValidateDuplicatedBrand() throws Exception {
        mockMvc.perform(put("/v1/brands/{id}", id2)
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

    @Test
    void shouldFailOnNonExistentBrand() throws Exception {
        mockMvc.perform(put("/v1/brands/{id}", UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "thebrand"
                        }
                        """)
        ).andExpect(status().isNotFound())
        ;
    }
}