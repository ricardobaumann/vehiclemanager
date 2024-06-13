package com.github.ricardobaumann.vehiclemanager.controllers;

import com.github.ricardobaumann.vehiclemanager.dtos.input.CreateBrandCommand;
import com.github.ricardobaumann.vehiclemanager.entities.Brand;
import com.github.ricardobaumann.vehiclemanager.services.BrandService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/v1/brands")
public class BrandController {
    private final BrandService brandService;

    @PostMapping
    @SneakyThrows
    public ResponseEntity<Void> create(@RequestBody @Valid CreateBrandCommand createBrandCommand) {

        Brand brand = brandService.create(createBrandCommand);

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/v1/brands/")
                        .path(brand.getId().toString()).build()
                        .toUri()).build();
    }

}
