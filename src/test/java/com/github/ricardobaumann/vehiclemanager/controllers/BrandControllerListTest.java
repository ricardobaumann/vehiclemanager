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
class BrandControllerListTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestObjects testObjects;

    @Autowired
    private BrandRepo brandRepo;
    private final UUID id1 = UUID.randomUUID();
    private final UUID id2 = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        testObjects.cleanUp();

        Brand entity = new Brand();
        entity.setName("thebrand1");
        entity.setId(id1);

        brandRepo.save(entity);

        Brand entity2 = new Brand();
        entity2.setName("thebrand2");
        entity2.setId(id2);

        brandRepo.save(entity2);
    }

    @Test
    void shouldListWithDefaultParams() throws Exception {
        mockMvc.perform(get("/v1/brands")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(String.format("""
                        {"totalPages":1,"totalElements":2,"first":true,"last":true,"size":20,
                        "content": [
                            {"id":"%s","name":"thebrand1"},
                            {"id":"%s","name":"thebrand2"}],
                        "number":0,"sort":{"empty":true,"unsorted":true,"sorted":false},
                        "pageable":{"pageNumber":0,"pageSize":20,
                            "sort":{"empty":true,"unsorted":true,"sorted":false},"offset":0,"unpaged":false,"paged":true},
                            "numberOfElements":2,"empty":false}
                        """, id1, id2)));
    }

    @Test
    void shouldListWithPagination() throws Exception {
        mockMvc.perform(get("/v1/brands?size=1&page=1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(String.format("""
                        {"totalPages":2,"totalElements":2,"first":false,"last":true,"size":1,
                        "content": [
                            {"id":"%s","name":"thebrand2"}],
                        "number":1,"sort":{"empty":true,"unsorted":true,"sorted":false},
                        "pageable":{"pageNumber":1,"pageSize":1,
                            "sort":{"empty":true,"unsorted":true,"sorted":false},"offset":1,"unpaged":false,"paged":true},
                            "numberOfElements":1,"empty":false}
                        """, id2)));
    }
}