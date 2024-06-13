package com.github.ricardobaumann.vehiclemanager.controllers;

import com.github.ricardobaumann.vehiclemanager.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class BrandControllerCreateTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnCreatedOnValidBrand() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/brands")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "foobar"
                                }
                                """)
                ).andExpect(status().isCreated())
                .andExpect(header().exists("location"));
    }

    @Test
    void shouldReturnBadRequestOnInvalidBrand() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/brands")
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
}