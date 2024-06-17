package com.github.ricardobaumann.vehiclemanager.controllers;

import com.github.ricardobaumann.vehiclemanager.dtos.input.CreateVehicleCommand;
import com.github.ricardobaumann.vehiclemanager.dtos.output.VehicleListResult;
import com.github.ricardobaumann.vehiclemanager.dtos.output.VehicleResult;
import com.github.ricardobaumann.vehiclemanager.entities.Vehicle;
import com.github.ricardobaumann.vehiclemanager.mappers.ResourceMapper;
import com.github.ricardobaumann.vehiclemanager.mappers.VehicleMapper;
import com.github.ricardobaumann.vehiclemanager.services.VehicleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    private final ResourceMapper resourceMapper;
    private final VehicleMapper vehicleMapper;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid CreateVehicleCommand createVehicleCommand) {
        Vehicle vehicle = vehicleService.create(createVehicleCommand);
        return resourceMapper.createdFrom(vehicle.getId());
    }

    @PutMapping("/{id}")
    public void update(@PathVariable UUID id,
                       @RequestBody @Valid CreateVehicleCommand createVehicleCommand) {
        vehicleService.update(id, createVehicleCommand);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResult> get(@PathVariable UUID id) {
        return vehicleService.getByID(id)
                .map(vehicleMapper::map)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public Page<VehicleListResult> list(Pageable pageable) {
        return vehicleService.list(pageable)
                .map(vehicleMapper::mapToList);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        vehicleService.delete(id);
    }

}
