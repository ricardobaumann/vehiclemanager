package com.github.ricardobaumann.vehiclemanager.controllers;

import com.github.ricardobaumann.vehiclemanager.dtos.input.CreateModelCommand;
import com.github.ricardobaumann.vehiclemanager.dtos.output.ModelResult;
import com.github.ricardobaumann.vehiclemanager.entities.Model;
import com.github.ricardobaumann.vehiclemanager.mappers.ModelMapper;
import com.github.ricardobaumann.vehiclemanager.mappers.ResourceMapper;
import com.github.ricardobaumann.vehiclemanager.services.ModelService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("v1/models")
public class ModelController {
    private final ModelService modelService;
    private final ResourceMapper resourceMapper;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid CreateModelCommand createModelCommand) {
        Model model = modelService.create(createModelCommand);
        return resourceMapper.createdFrom(model.getId());
    }

    @PutMapping("/{id}")
    public void update(@PathVariable UUID id,
                       @RequestBody @Valid CreateModelCommand createModelCommand) {
        modelService.update(id, createModelCommand);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelResult> get(@PathVariable UUID id) {
        return modelService.getByID(id)
                .map(modelMapper::map)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public Page<ModelResult> list(Pageable pageable) {
        return modelService.list(pageable).map(modelMapper::map);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        modelService.delete(id);
    }
}
