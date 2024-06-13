package com.github.ricardobaumann.vehiclemanager.controllers;

import com.github.ricardobaumann.vehiclemanager.TestcontainersConfiguration;
import com.github.ricardobaumann.vehiclemanager.entities.Brand;
import com.github.ricardobaumann.vehiclemanager.repos.BrandRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
@Commit
class BrandControllerDeleteTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BrandRepo brandRepo;

    private final UUID id = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        brandRepo.deleteAll();
        Brand entity = new Brand();
        entity.setId(id);
        entity.setName("thebrand");
        brandRepo.save(entity);
    }

    @Test
    void shouldDeleteExistentBrand() throws Exception {
        mockMvc.perform(delete("/v1/brands/{id}", id))
                .andExpect(status().isOk());
        assertThat(brandRepo.count()).isZero();
    }

    @Test
    void shouldSkipNonExistentBrand() throws Exception {
        mockMvc.perform(delete("/v1/brands/{id}", UUID.randomUUID()))
                .andExpect(status().isOk());
        assertThat(brandRepo.count()).isOne();
    }
}