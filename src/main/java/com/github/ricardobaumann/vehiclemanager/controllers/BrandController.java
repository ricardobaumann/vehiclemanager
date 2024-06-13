package com.github.ricardobaumann.vehiclemanager.controllers;

import com.github.ricardobaumann.vehiclemanager.dtos.input.CreateBrandCommand;
import com.github.ricardobaumann.vehiclemanager.dtos.output.BrandResult;
import com.github.ricardobaumann.vehiclemanager.entities.Brand;
import com.github.ricardobaumann.vehiclemanager.mappers.BrandMapper;
import com.github.ricardobaumann.vehiclemanager.services.BrandService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/v1/brands")
public class BrandController {
    private final BrandService brandService;
    private final BrandMapper brandMapper;

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

    @PutMapping("/{id}")
    public void put(
            @PathVariable UUID id,
            @RequestBody @Valid CreateBrandCommand createBrandCommand) {
        brandService.update(id, createBrandCommand);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResult> get(@PathVariable UUID id) {
        return brandService.getById(id)
                .map(brandMapper::map)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public Page<BrandResult> list(Pageable pageable) {
        return brandService.list(pageable)
                .map(brandMapper::map);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        brandService.delete(id);
    }

}
